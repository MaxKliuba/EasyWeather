package com.maxclub.easyweather;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.google.android.material.snackbar.Snackbar;
import com.maxclub.easyweather.database.model.City;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CityListFragment extends Fragment {

    RecyclerView mRecyclerView;
    private Adapter mAdapter;

    public static CityListFragment newInstance() {
        return new CityListFragment();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_list, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.city_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);

        CityViewModel cityViewModel = new ViewModelProvider(this).get(CityViewModel.class);
        cityViewModel.getCityLiveData().observe(getViewLifecycleOwner(), new Observer<List<City>>() {
            @Override
            public void onChanged(List<City> cityList) {
                mAdapter.setItems(cityList);
                mAdapter.getItems().add(cityViewModel.getCurrentLocationCity());
                mAdapter.notifyDataSetChanged();
            }
        });

        enableSwipeToDeleteAndUndo();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.city_list_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.action_save:
                Intent SearchWeatherActivityIntent = SearchWeatherActivity.newIntent(getActivity());
                startActivity(SearchWeatherActivityIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.CityViewHolder> {
        private static final int CURRENT_LOCATION = 0;
        private static final int CITY = 1;

        private SortedList<City> mCities;

        public Adapter() {
            mCities = new SortedList<>(City.class, new SortedList.Callback<City>() {
                @Override
                public int compare(City o1, City o2) {
                    return o1.order - o2.order;
                }

                @Override
                public void onChanged(int position, int count) {
                    notifyItemRangeChanged(position, count);
                }

                @Override
                public boolean areContentsTheSame(City oldItem, City newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areItemsTheSame(City item1, City item2) {
                    return item1.id == item2.id;
                }

                @Override
                public void onInserted(int position, int count) {
                    notifyItemRangeChanged(position, count);
                }

                @Override
                public void onRemoved(int position, int count) {
                    notifyItemRangeChanged(position, count);
                }

                @Override
                public void onMoved(int fromPosition, int toPosition) {
                    notifyItemMoved(fromPosition, toPosition);
                }
            });
        }

        public SortedList<City> getItems() {
            return mCities;
        }

        public void setItems(List<City> cities) {
            mCities.replaceAll(cities);
        }

        @NonNull
        @NotNull
        @Override
        public CityViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view;
            if (viewType == CURRENT_LOCATION) {
                view = layoutInflater.inflate(R.layout.list_item_location, parent, false);
            } else {
                view = layoutInflater.inflate(R.layout.list_item_city, parent, false);
            }

            return new CityViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull CityListFragment.Adapter.CityViewHolder holder, int position) {
            holder.bind(mCities.get(position));
        }

        @Override
        public int getItemCount() {
            return mCities.size();
        }

        @Override
        public int getItemViewType(int position) {
            City city = mCities.get(position);

            return city.id == 0 ? CURRENT_LOCATION : CITY;
        }

        private class CityViewHolder extends RecyclerView.ViewHolder {
            private TextView mCityNameTextView;
            private City mCity;

            public CityViewHolder(@NonNull @NotNull View itemView, int viewType) {
                super(itemView);

                mCityNameTextView = itemView.findViewById(R.id.city_name_view);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = WeatherPagerActivity.newIntent(getActivity(), mCity);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
            }

            public void bind(City city) {
                mCity = city;
                if (city.country != null) {
                    mCityNameTextView.setText(String.format("%s, %s", city.name, city.country));
                } else {
                    mCityNameTextView.setText(city.name);
                }
            }
        }
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final City city = mAdapter.getItems().get(position);
                App.getInstance().getCityDao().delete(city);

                Snackbar snackbar = Snackbar.make(mRecyclerView, getString(R.string.snackbar_delete_text), Snackbar.LENGTH_LONG);
                snackbar.setAction(getString(R.string.snackbar_undo_text), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        App.getInstance().getCityDao().insert(city);
                    }
                });
                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.grey));
                snackbar.setTextColor(Color.WHITE);
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(mRecyclerView);
    }
}
