package com.heyi.framework.epc;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author sulta
 *
 * @param <E>
 */
public abstract class AbstractEpcServiceManager<E extends EpcEvent> implements EpcServiceManager {

	protected final static Logger logger = LoggerFactory.getLogger(AbstractEpcServiceManager.class);

	protected abstract Iterator<EpcFunction<E>> iterator();
	
	public void execute(E event) {

		EpcContext<E> context = new EpcContext<E>(event, this);

		for (Iterator<EpcFunction<E>> iterator = iterator(); iterator.hasNext();) {
			EpcFunction<E> epcFunction = iterator.next();

			if (epcFunction.supports(context.getSource())) {
				if (logger.isDebugEnabled()) {
					logger.debug("正在执行功能：{}", epcFunction.getClass().getName());
				}

				try {
					epcFunction.execute(context);
				} finally {
					if(context.isFireNextEvent) epcFunction.nextEvent(context);
					context.reset(); 
				}
				break; //在一个过程中，一个事件只能由唯一一个功能来响应。
			}
		}
	}
}
