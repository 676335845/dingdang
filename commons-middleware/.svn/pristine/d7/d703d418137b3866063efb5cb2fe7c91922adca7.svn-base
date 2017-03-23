package com.heyi.framework.epc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author sulta
 *
 * @param <E>
 */
public class EpcFunctionsList<E extends EpcEvent> implements Iterable<EpcFunction<E>> {

	protected final List<EpcFunction<E>> functions = new ArrayList<>();

	public EpcFunctionsList() {

	}

	public EpcFunctionsList(List<EpcFunction<E>> functions) {
		addAll(functions);
	}

	public EpcFunctionsList(EpcFunctionsList<E> list) {
		addAll(list.functions);
	}

	public void add(EpcFunction<E> func) {
		functions.add(func);
	}

	public void addAll(List<EpcFunction<E>> functions) {
		for (EpcFunction<E> fuc : functions) {
			add(fuc);
		}
	}
	
	@Override
	public Iterator<EpcFunction<E>> iterator() {
		return functions.iterator();
	}

}
