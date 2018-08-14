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
 * File Created on 13/08/18 9:00 PM by nishant
 * Last Modified on 13/08/18 9:00 PM
 */

package com.example.nishant.berry.data.component;

import com.example.nishant.berry.data.DataManager;
import com.example.nishant.berry.data.module.AccountUseCaseModule;
import com.example.nishant.berry.data.module.ChatUseCaseModule;
import com.example.nishant.berry.data.module.DataManagerModule;
import com.example.nishant.berry.data.module.FriendsUseCaseModule;
import com.example.nishant.berry.data.module.InteractionUseCaseModule;
import com.example.nishant.berry.data.module.ProfileUseCaseModule;
import com.example.nishant.berry.data.module.RequestUseCaseModule;
import com.example.nishant.berry.data.module.SearchUseCaseModule;
import com.example.nishant.berry.data.module.SettingsUseCaseModule;

import dagger.Component;

@Component(modules = {
        DataManagerModule.class,
        AccountUseCaseModule.class,
        ChatUseCaseModule.class,
        FriendsUseCaseModule.class,
        InteractionUseCaseModule.class,
        ProfileUseCaseModule.class,
        RequestUseCaseModule.class,
        SearchUseCaseModule.class,
        SettingsUseCaseModule.class})
public interface DataManagerComponent {
    DataManager getDataManager();
}
