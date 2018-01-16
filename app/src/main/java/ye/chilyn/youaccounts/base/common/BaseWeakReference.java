package ye.chilyn.youaccounts.base.common;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * 持有弱引用的基类，防止内存泄漏
 * @param <T> 弱引用的泛型
 */
public abstract class BaseWeakReference<T> {

	private Reference<T> mReference;
	
	public BaseWeakReference(T t) {
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
