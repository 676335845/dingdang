package com.heyi.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class AnnotationUtil {
	public static Set<Class<?>> getClasses(String pack) 
			throws ClassNotFoundException, IOException {

		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();

		boolean recursive = true;

		String packageName = pack;
		String packageDirName = packageName.replace('.', '/');

		Enumeration<URL> dirs;
		dirs = Thread.currentThread().getContextClassLoader()
				.getResources(packageDirName);

		while (dirs.hasMoreElements()) {

			URL url = dirs.nextElement();

			String protocol = url.getProtocol();

			if ("file".equals(protocol)) {
				System.err.println("file类型的扫描");

				String filePath = URLDecoder.decode(url.getFile(), "UTF-8");

				findAndAddClassesInPackageByFile(packageName, filePath,
						recursive, classes);
			} else if ("jar".equals(protocol)) {
				System.err.println("jar类型的扫描");
				JarFile jar;
				JarURLConnection jarURLConnection = (JarURLConnection) url
						.openConnection();
				jar = jarURLConnection.getJarFile();
				Enumeration<JarEntry> entries = jar.entries();
				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					String name = entry.getName();
					if (name.charAt(0) == '/') {
						name = name.substring(1);
					}
					if (name.startsWith(packageDirName)) {
						int idx = name.lastIndexOf('/');
						if (idx != -1) {
							packageName = name.substring(0, idx)
									.replace('/', '.');
						}
						if ((idx != -1) || recursive) {
							if (name.endsWith(".class")
									&& !entry.isDirectory()) {
								String className = name.substring(
										packageName.length() + 1,
										name.length() - 6);
								classes.add(Class.forName(packageName + '.'
												+ className));
							}
						}
					}
				}
			}
		}
		
		return classes;
		}

		/**
		 * 以文件的形式来获取包下的所有Class
		 * 
		 * @param packageName
		 * @param packagePath
		 * @param recursive
		 * @param classes
		 * @throws ClassNotFoundException 
		 */
		private static void findAndAddClassesInPackageByFile(String packageName,
				String packagePath, final boolean recursive, Set<Class<?>> classes) 
						throws ClassNotFoundException {
			
			File dir = new File(packagePath);
			if (!dir.exists() || !dir.isDirectory()) {
				return;
			}
			File[] dirfiles = dir.listFiles(myFilter);
			for (File file : dirfiles) {
				if (file.isDirectory()) {
					findAndAddClassesInPackageByFile(
							packageName + "." + file.getName(),
							file.getAbsolutePath(), recursive, classes);
				} else {
					String className = file.getName().substring(0,
							file.getName().length() - 6);
					
					classes.add(Thread.currentThread().getContextClassLoader()
							.loadClass(packageName + '.' + className));
				}
			}
		}
		
		private static MyFilter myFilter = new MyFilter(true);
		
		/**
		 * 过滤得到需要的class文件
		 *
		 */
		static class MyFilter implements FileFilter {
			
			private boolean recursive;
			
			public MyFilter(boolean recursive) {
				this.recursive = recursive;
			}
			
			public boolean accept(File file) {
				return (recursive && file.isDirectory())
						|| (file.getName().endsWith(".class") 
								&& file.getName().contains("$") == false);
			}
		}
}
