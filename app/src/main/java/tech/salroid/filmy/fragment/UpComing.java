package tech.salroid.filmy.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import tech.salroid.filmy.R;
import tech.salroid.filmy.activities.MovieDetailsActivity;
import tech.salroid.filmy.custom_adapter.MainActivityAdapter;
import tech.salroid.filmy.customs.BreathingProgress;
import tech.salroid.filmy.database.FilmContract;
import tech.salroid.filmy.database.MovieProjection;

/*
 * Filmy Application for Android
 * Copyright (c) 2016 Ramankit Singh (http://github.com/webianks).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class UpComing extends Fragment implements MainActivityAdapter.ClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private MainActivityAdapter mainActivityAdapter;

    @BindView(R.id.recycler)
    RecyclerView recycler;
    public UpComing() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_up_coming, container, false);
        ButterKnife.bind(this,view);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recycler.setLayoutManager(gridLayoutManager);

        mainActivityAdapter = new MainActivityAdapter(getActivity(), null);
        recycler.setAdapter(mainActivityAdapter);
        mainActivityAdapter.setClickListener(this);

        return view;

    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().initLoader(MovieProjection.UPCOMING_MOVIE_LOADER, null, this);
    }

    @Override
    public void itemClicked(Cursor cursor) {

        int id_index = cursor.getColumnIndex(FilmContract.MoviesEntry.MOVIE_ID);
        int title_index = cursor.getColumnIndex(FilmContract.MoviesEntry.MOVIE_TITLE);


        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra("title", cursor.getString(title_index));
        intent.putExtra("activity", true);
        intent.putExtra("type", 2);
        intent.putExtra("database_applicable", true);
        intent.putExtra("network_applicable", true);
        intent.putExtra("id", cursor.getString(id_index));
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri moviesForTheUri = FilmContract.UpComingMoviesEntry.CONTENT_URI;


        return new CursorLoader(getActivity(),
                moviesForTheUri,
                MovieProjection.MOVIE_COLUMNS,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (mainActivityAdapter != null)
            mainActivityAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mainActivityAdapter != null)
            mainActivityAdapter.swapCursor(null);
    }


}