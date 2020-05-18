/*
 * The MIT License (MIT)
 * Copyright © 2012 Steve Guidetti
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the “Software”), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.ultramegatech.ey;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ultramegatech.ey.util.CommonMenuHandler;
import com.ultramegatech.ey.util.PreferenceUtils;

/**
 * This Activity displays the list of elements. On large screens, this also shows the details for
 * the selected element in a second pane.
 *
 * @author Steve Guidetti
 */
public class ElementListActivity extends AppCompatActivity {
    /**
     * Whether the Activity has a two-pane layout
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final boolean darkTheme = PreferenceUtils.getPrefDarkTheme();
        setTheme(darkTheme ? R.style.DarkTheme : R.style.LightTheme);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_element_list);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (findViewById(R.id.elementDetails) != null) {
            mTwoPane = true;
            final ElementListFragment fragment = (ElementListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.elementList);
            if (fragment != null) {
                fragment.setActivateOnItemClick(true);
            }
        }
    }

    public void onItemSelected(int id) {
        if(mTwoPane) {
            final Fragment fragment = ElementDetailsFragment.getInstance(id);
            getSupportFragmentManager().beginTransaction().replace(R.id.elementDetails, fragment)
                    .commit();
        } else {
            final Intent intent = new Intent(this, ElementDetailsActivity.class);
            intent.putExtra(ElementDetailsActivity.EXTRA_ATOMIC_NUMBER, id);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.element_list, menu);
        inflater.inflate(R.menu.common, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
            case R.id.menu_table:
                finish();
                return true;
        }
        return CommonMenuHandler.handleSelect(this, id) || super.onOptionsItemSelected(item);
    }
}
