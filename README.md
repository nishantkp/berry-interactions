# berry-interactions

[![Build Status](https://travis-ci.com/nishantkp/berry-interactions.svg?token=2FXfJV7LpgiJJ6nPFCCt&branch=master)](https://travis-ci.com/nishantkp/berry-interactions)

Chat app with firebase.

#### Helpers :smiley:

- Android dataBinding  `(@BindingAdapters + twoWayDatabinding)`
- Lazy @SingleTon pattern
- Firebase `LOL`
- Picasso for image loading
- RxAndroid2
- DiffUtils.Callback `@RecyclerView Adapter`
- Dagger2 `::FUTURE UPDATE::`
- Test cases `::FUTURE UPDATE::`
- Java Lambdas `::FUTURE UPDATE::`
- Kotlin Code-base `::FUTURE UPDATE::`

#### DiffUtils.Callback `RecyclerView++` :smiley:

As of 24.2.0, RecyclerView support library, v7 package offers really handy utility class called DiffUtil. This class finds the difference between two lists and provides the updated list as an output. This class is used to notify updates to a RecyclerView Adapter.
<br>For detailed information click [here](https://android.jlelse.eu/smart-way-to-update-recyclerview-using-diffutil-345941a160e0).</br>

```java
public class UserDiffCallback extends DiffUtil.Callback {
    protected User[] oldList, newList;

    public UserDiffCallback(User[] oldList, User[] newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList == null ? 0 : oldList.length;
    }

    @Override
    public int getNewListSize() {
        return newList == null ? 0 : newList.length;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList[oldItemPosition].equals(newList[newItemPosition]);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }
}
```
###### Update RecyclerView adapter with,
```java
public void updateData(List<AllUsers> data) {
    // DiffUtils callbacks for calculating difference between new batch of data and old data
    // So we can update only specific views rather that updating whole list with
    // notifyDataSetChanged()
    DiffUtil.DiffResult diffResult =
            DiffUtil.calculateDiff(new UserDiffCallback(mData, data));
    mData.clear();
    mData.addAll(data);
    diffResult.dispatchUpdatesTo(this);
}
```

#### MVP Model

![berry-model](/ux/berry-model.jpg)

#### UX design
###### * Users are just for fun :smiley:
<img src="/ux/start_screen.png" width="200"> <img src="/ux/sign_in.png" width="200"> <img src="/ux/sign_up.png" width="200"> <img src="/ux/interactions_4.png" width="200"> <img src="/ux/user_search_1.png" width="200"> <img src="/ux/account_settings.png" width="200">

#### License 
```
MIT License

Copyright (c) 2018 Nishant Patel

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
