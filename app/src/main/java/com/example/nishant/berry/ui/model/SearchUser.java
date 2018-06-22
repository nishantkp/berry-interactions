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
 * File Created on 21/06/18 6:57 PM by nishant
 * Last Modified on 21/06/18 6:57 PM
 */

package com.example.nishant.berry.ui.model;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

import com.android.databinding.library.baseAdapters.BR;
import com.example.nishant.berry.ui.search.SearchActivity;

/**
 * Model for {@link SearchActivity} layout
 * Two way data binding
 * This model implements Observable interface so that we can get text entered by user
 * in EditText field in real time
 * So that we don't need to implement TextWatcher on EditText field
 * i.e as soon as text in EditText field gets changed, we get the text
 */
public class SearchUser
        implements Observable {

    private String searchQuery;
    private PropertyChangeRegistry registry = new PropertyChangeRegistry();

    @Bindable
    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
        registry.notifyChange(this, BR.searchQuery);
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.remove(callback);
    }
}
