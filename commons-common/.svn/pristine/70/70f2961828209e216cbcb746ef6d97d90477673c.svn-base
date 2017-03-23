package com.heyi.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ClassUtil {
	private final static String POINT = ".";
	private final static String CLASS = ".class";
	
	// class文件过滤器
	private static MyFilter myFilter = new MyFilter(true);
	
	/**
	 * 获取某个包下所有的class信息
	 * @param pack
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Set<Class<?>> getClasses(String pack) 
			throws ClassNotFoundException, IOException {
		// 第一个class类的集合
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		// 是否循环迭代
		boolean recursive = true;
		// 获取包的名字，并进行替换
		String packageName = pack;
		String packageDirName = packageName.replace('.', '/');
		// 定义一个枚举的集合，并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		dirs = Thread.currentThread().getContextClassLoader()
				.getResources(packageDirName);
		// 循环迭代下去
		while (dirs.hasMoreElements()) {
			// 获取下一个元素
			URL url = dirs.nextElement();
			// 得到协议的名称
			String protocol = url.getProtocol();
			// 如果是以文件的形式保存在文件系统上
			if ("file".equals(protocol)) {
				System.err.println("file类型的扫描");
				// 获取包的物理路径
				String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
				// 以文件的方式扫描整个包下的文件，并添加到集合中
				findAndAddClassesInPackageByFile(packageName, filePath,
						recursive, classes);
			} else if ("jar".equals(protocol)) {
				// 如果是jar包文件
				// 定义一个JarFile
				System.err.println("jar类型的扫描");
				JarFile jar;
				// 获取jar
				JarURLConnection jarURLConnection = (JarURLConnection) url
						.openConnection();
				jar = jarURLConnection.getJarFile();
				// 从此jar包，得到一个枚举类
				Enumeration<JarEntry> entries = jar.entries();
				// 同样的进行循环迭代
				while (entries.hasMoreElements()) {
					// 获取jar里的一个实体，可以是目录，和一些jar包里的其他文件，如META-INF等文件
					JarEntry entry = entries.nextElement();
					String name = entry.getName();
					// 如果是以/开头的
					if (name.charAt(0) == '/') {
						// 获取后面的字符串
						name = name.substring(1);
					}
					// 如果前半部分和定义的包名相同
					if (name.startsWith(packageDirName)) {
						int idx = name.lastIndexOf('/');
						// 如果以"/"结尾，是一个包
						if (idx != -1) {
							// 获取包名，把"/"替换成"."
							packageName = name.substring(0, idx)
									.replace('/', '.');
						}
						// 如果可以迭代下去，并且是一个包
						if ((idx != -1) || recursive) {
							// 如果是一个.class文件，而且不是目录
							if (name.endsWith(".class")
									&& !entry.isDirectory()) {
								// 去掉后面的".class"，获取真正的类名
								String className = name.substring(
										packageName.length() + 1,
										name.length() - 6);
								// 添加到classes
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
		
		// 获取此包的目录，建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者，也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		// 如果存在，就获取包下的所有文件，包括目录
		File[] dirfiles = dir.listFiles(myFilter);
		// 循环所有文件
		for (File file : dirfiles) {
			// 如果是目录，则继续扫描
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(
						packageName + "." + file.getName(),
						file.getAbsolutePath(), recursive, classes);
			} else {
				// 如果是java类文件，去掉后面的.class，只留下类名
				String className = file.getName().substring(0,
						file.getName().length() - 6);
				
				// 添加到集合中去
				classes.add(Thread.currentThread().getContextClassLoader()
						.loadClass(packageName + '.' + className));
			}
		}
	}
	
	public static Object deepClone(Object obj) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			bis = new ByteArrayInputStream(bos.toByteArray());
			ois = new ObjectInputStream(bis);
			return ois.readObject();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (oos != null) {
					oos.close();
				}
				if (bis != null) {
					bis.close();
				}
				if (ois != null) {
					ois.close();
				}
			} catch (IOException e) {
				;// ignore
			}
		}
	}

	/**
	 * 获得泛型类型
	 * 
	 * @param object
	 * @return
	 */
	public static Class<?> getGenericType(Class<?> clazz) {
		if (clazz == Object.class) {
			return null;
		}
		Type type = clazz.getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			ParameterizedType ptype = ((ParameterizedType) type);
			Type[] args = ptype.getActualTypeArguments();
			return (Class<?>) args[0];
		}
		return getGenericType(clazz.getSuperclass());
	}

	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = ClassUtil.class.getClassLoader();
			if (cl == null) {
				// getClassLoader() returning null indicates the bootstrap
				// ClassLoader
				try {
					cl = ClassLoader.getSystemClassLoader();
				} catch (Throwable ex) {
					// Cannot access system ClassLoader - oh well, maybe the
					// caller can live with null...
				}
			}
		}
		return cl;
	}
}


/**
 * 过滤得到需要的class文件
 *
 */
class MyFilter implements FileFilter {
	
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