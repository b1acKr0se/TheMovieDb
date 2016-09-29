package nt.hai.themoviedb.ui.base;

public class Presenter<T extends BaseView> implements BasePresenter<T> {
    private T view;
    @Override
    public void attachView(T view) {
        this.view = view;
    }
    @Override
    public void detachView() {
        this.view = null;
    }
    public T getView() {
        return view;
    }
    public boolean isViewAttached() {
        return view != null;
    }
    public void checkViewAttached() {
        if (!isViewAttached()) throw new ViewNotAttachedException();
    }
}