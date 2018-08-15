/*
 * Copyright (c) 2018
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * File Created on 27/06/18 6:53 PM by nishant
 * Last Modified on 27/06/18 6:53 PM
 */

package com.example.nishant.berry.data;

import android.support.annotation.NonNull;

import com.example.nishant.berry.data.callbacks.OnUsersData;
import com.example.nishant.berry.data.callbacks.OnUsersList;
import com.example.nishant.berry.ui.dashboard.fragment.friends.FriendsFragment;
import com.example.nishant.berry.ui.model.AllUsers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * FriendsUtility class to get the friends list of current user in {@link FriendsFragment}
 * with the help of FirebaseRecyclerAdapter
 */
@Singleton
public final class FriendsUseCase {
    private FbUsersUseCase mFbUsersUseCase;

    @Inject
    public FriendsUseCase(FbUsersUseCase fbUsersUseCase) {
        mFbUsersUseCase = fbUsersUseCase;
    }

    //--------------------------------------------------------------------------------------------//
    //-----------------------------------Firebase calls with Rx-----------------------------------//
    //--------------------------------------------------------------------------------------------//

    /**
     * Call this method to get the data from user id
     * FlatMapIterable to loop though list of Ids, then makes parallel calls to fetch information
     * about user and in the end converts the results of those parallel calls to list.
     */
    void getFriends(@NonNull final OnUsersList callback) {
        getFriendsIds(callback).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(new Function<List<String>, Iterable<String>>() {
                    @Override
                    public Iterable<String> apply(List<String> strings) throws Exception {
                        return strings;
                    }
                })
                .flatMap(new Function<String, ObservableSource<AllUsers>>() {
                    @Override
                    public ObservableSource<AllUsers> apply(String s) throws Exception {
                        return getUserInfo(s, callback);
                    }
                })
                .toList()
                .subscribe(new SingleObserver<List<AllUsers>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<AllUsers> allUsers) {
                        callback.onData(allUsers);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }
                });
    }

    /**
     * Call this method to get the friends id list from firebase
     * Must call Emitters onComplete() after sending results with onNext() to indicate the task
     * is complete
     *
     * @return Observable object for getting the list of friend's id
     */
    private Observable<List<String>> getFriendsIds(final @NonNull OnUsersList callback) {
        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<String>> emitter) {
                Query query = mFbUsersUseCase.getCurrentUserFriendsRef();
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> friendIds = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            friendIds.add(data.getKey());
                        }
                        // list of ids
                        emitter.onNext(friendIds);
                        emitter.onComplete();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onError(databaseError.getMessage());
                    }
                });
            }
        });
    }

    /**
     * Call this method get information about user from user's id
     * Observable must subscribe on Schedulers.io() so that when all the parallel calls are completed
     * we can combine the results to make a list
     * Must call Emitters onComplete() after sending results with onNext() to indicate the task
     * is complete
     *
     * @param id UserId
     * @return Observable for fetching data about specific user
     */
    private ObservableSource<AllUsers> getUserInfo(final @NonNull String id,
                                                   final @NonNull OnUsersList callback) {
        return Observable.create(new ObservableOnSubscribe<AllUsers>() {
            @Override
            public void subscribe(final ObservableEmitter<AllUsers> emitter) throws Exception {
                mFbUsersUseCase.getUsersObject(id, new OnUsersData() {
                    @Override
                    public void onData(AllUsers model, String userId) {
                        // Getting all users here
                        model.setId(id);
                        emitter.onNext(model);
                        emitter.onComplete();
                    }

                    @Override
                    public void onError(String error) {
                        callback.onError(error);
                    }
                });
            }
        }).subscribeOn(Schedulers.io());
    }

    //--------------------------------------------------------------------------------------------//
    //-----------------------------------Regular Firebase calls-----------------------------------//
    //--------------------------------------------------------------------------------------------//

    /**
     * Call this method to get all the friends
     *
     * @param callback DataCallback for list of friends and error
     */
    void getAllFriends(@NonNull final OnUsersList callback) {
        Query query = mFbUsersUseCase.getCurrentUserFriendsRef();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<AllUsers> list = new LinkedList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    getInfoFromId(data.getKey(), list, callback);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }

    /**
     * Use this method to get the detail info about user from it's Id from firebase database
     *
     * @param userId   userId
     * @param list     empty list of friends
     * @param callback DataCallback for list of friends and error
     */
    private void getInfoFromId(final String userId, final List<AllUsers> list, @NonNull final OnUsersList callback) {
        mFbUsersUseCase.getUsersObject(userId, new OnUsersData() {
            @Override
            public void onData(AllUsers model, String userId) {
                model.setId(userId);
                // If user doesn't exists in the list, add it
                if (!list.contains(model)) {
                    list.add(model);
                } else {
                    // otherwise update the values at user's current index
                    int index = list.indexOf(model);
                    list.set(index, model);
                }
                callback.onData(list);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }
}
