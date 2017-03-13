package com.github.aleksandermielczarek.objectboxexample.util;

import android.support.v7.util.DiffUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList;

/**
 * Created by Aleksander Mielczarek on 12.03.2017.
 */

public final class ViewModelDiff<T> {

    private final DiffObservableList<T> oldViewModels;
    private final List<T> newViewModels;
    private final DiffUtil.DiffResult diffResult;

    private ViewModelDiff(DiffObservableList<T> oldViewModels, List<T> newViewModels, DiffUtil.DiffResult diffResult) {
        this.oldViewModels = oldViewModels;
        this.newViewModels = newViewModels;
        this.diffResult = diffResult;
    }

    public void updateList() {
        oldViewModels.update(newViewModels, diffResult);
    }

    public static <M, VM> Function<List<M>, SingleSource<? extends List<VM>>> modelsToViewModels(Function<M, VM> modelToViewModel) {
        return models -> Observable.fromIterable(models)
                .map(modelToViewModel)
                .toList();
    }

    public static <VM> Function<List<VM>, ViewModelDiff<VM>> calculateViewModelsDiff(DiffObservableList<VM> oldViewModels) {
        return newViewModels -> {
            DiffUtil.DiffResult diffResult = oldViewModels.calculateDiff(newViewModels);
            return new ViewModelDiff<>(oldViewModels, newViewModels, diffResult);
        };
    }

    public static <M, ID> DiffObservableList.Callback<M> idEqualsCallback(Function<M, ID> modelId) {
        return new IdEqualsCallback<>(modelId);
    }

    public static <VM, M, ID> DiffObservableList.Callback<VM> viewModelIdEqualsCallback(Function<M, ID> modelId, Function<VM, M> viewModelToModel) {
        return new ViewModelIdEqualsCallback<>(modelId, viewModelToModel);
    }

    private static final class IdEqualsCallback<M, ID> implements DiffObservableList.Callback<M> {

        private final Function<M, ID> modelId;

        private IdEqualsCallback(Function<M, ID> modelId) {
            this.modelId = modelId;
        }

        @Override
        public boolean areItemsTheSame(M model1, M model2) {
            try {
                ID idModel1 = modelId.apply(model1);
                ID idModel2 = modelId.apply(model2);
                return idModel1.equals(idModel2);
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public boolean areContentsTheSame(M model1, M model2) {
            return model1.equals(model2);
        }
    }

    private static final class ViewModelIdEqualsCallback<VM, M, ID> implements DiffObservableList.Callback<VM> {

        private final IdEqualsCallback<M, ID> idEqualsCallback;
        private final Function<VM, M> viewModelToModel;

        private ViewModelIdEqualsCallback(Function<M, ID> modelId, Function<VM, M> viewModelToModel) {
            idEqualsCallback = new IdEqualsCallback<>(modelId);
            this.viewModelToModel = viewModelToModel;
        }

        @Override
        public boolean areItemsTheSame(VM viewModel1, VM viewModel2) {
            try {
                M model1 = viewModelToModel.apply(viewModel1);
                M model2 = viewModelToModel.apply(viewModel2);
                return idEqualsCallback.areItemsTheSame(model1, model2);
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public boolean areContentsTheSame(VM viewModel1, VM viewModel2) {
            try {
                M model1 = viewModelToModel.apply(viewModel1);
                M model2 = viewModelToModel.apply(viewModel2);
                return idEqualsCallback.areContentsTheSame(model1, model2);
            } catch (Exception e) {
                return false;
            }
        }
    }
}
