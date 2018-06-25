package ye.chilyn.youaccount.base.common;

import android.os.Handler;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * 静态内部Handler的基类，持有弱引用，防止内存泄漏
 * @param <T> 弱引用的泛型
 */
public abstract class BaseStaticInnerHandler<T> extends Handler {

	private Reference<T> mReference;
	
	public BaseStaticInnerHandler(T t) {
		mReference = new WeakReference<T>(t);
	}
	
	public boolean isReferenceRecycled() {
		return (mReference == null) || (mReference.get() == null);
	}
	
	public T getReference() {
		return mReference.get();
	}

	public void clearReference() {
		if (mReference != null) {
			mReference.clear();
			mReference = null;
		}
	}
}
