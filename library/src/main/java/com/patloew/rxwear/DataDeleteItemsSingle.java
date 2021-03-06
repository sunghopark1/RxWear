package com.patloew.rxwear;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.TimeUnit;

import rx.SingleSubscriber;

/* Copyright 2016 Patrick Löwenstein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
public class DataDeleteItemsSingle extends BaseSingle<Integer> {

    private final Uri uri;
    private final Integer filterType;

    DataDeleteItemsSingle(RxWear rxWear, Uri uri, Integer filterType, Long timeout, TimeUnit timeUnit) {
        super(rxWear, timeout, timeUnit);
        this.uri = uri;
        this.filterType = filterType;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final SingleSubscriber<? super Integer> subscriber) {
        ResultCallback<DataApi.DeleteDataItemsResult> resultResultCallback = new ResultCallback<DataApi.DeleteDataItemsResult>() {
            @Override
            public void onResult(@NonNull DataApi.DeleteDataItemsResult deleteDataItemsResult) {
                if (!deleteDataItemsResult.getStatus().isSuccess()) {
                    subscriber.onError(new StatusException(deleteDataItemsResult.getStatus()));
                } else {
                    subscriber.onSuccess(deleteDataItemsResult.getNumDeleted());
                }
            }
        };

        if(filterType == null) {
            setupWearPendingResult(Wearable.DataApi.deleteDataItems(apiClient, uri), resultResultCallback);
        } else {
            setupWearPendingResult(Wearable.DataApi.deleteDataItems(apiClient, uri, filterType), resultResultCallback);
        }
    }
}
