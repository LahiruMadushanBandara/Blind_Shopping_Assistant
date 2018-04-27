

package com.beacon.shopping.assistant.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beacon.shopping.assistant.R;
import com.beacon.shopping.assistant.databinding.ItemTrackedBeaconBinding;
import com.beacon.shopping.assistant.model.ActionBeacon;
import com.beacon.shopping.assistant.model.IManagedBeacon;
import com.beacon.shopping.assistant.model.TrackedBeacon;
import com.beacon.shopping.assistant.ui.fragment.BeaconFragment;
import com.beacon.shopping.assistant.ui.fragment.TrackedBeaconsFragment;
import com.beacon.shopping.assistant.ui.view.WrapLinearLayoutManager;
import com.beacon.shopping.assistant.util.Constants;
import com.beacon.shopping.assistant.viewModel.TrackedBeaconViewModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lahiru on 09/12/2017.
 */
public class TrackedBeaconAdapter extends BeaconAdapter<TrackedBeaconAdapter.BindingHolder> {

    private Map<String, ActionBeaconAdapter> mActionAdapters = new HashMap<>();

    public TrackedBeaconAdapter(BeaconFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTrackedBeaconBinding beaconBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_tracked_beacon,
                parent,
                false);
        setupSwipe(beaconBinding);
        return new BindingHolder(beaconBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {
        ItemTrackedBeaconBinding beaconBinding = holder.binding;

        ActionBeaconAdapter adapter = new ActionBeaconAdapter((TrackedBeaconsFragment) mFragment);
        beaconBinding.recyclerActions.setLayoutManager(new WrapLinearLayoutManager(mFragment.getActivity()));
        beaconBinding.recyclerActions.setAdapter(adapter);

        TrackedBeacon beacon = (TrackedBeacon) getItem(position);
        adapter.setItems(beacon.getActions());

        mActionAdapters.put(beacon.getId(), adapter);

        holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onBeaconLongClickListener != null) {
                    onBeaconLongClickListener.onBeaconLongClick(position);
                }
                return false;
            }
        });

        beaconBinding.setViewModel(new TrackedBeaconViewModel(mFragment, beacon));
    }


    private void setupSwipe(ItemTrackedBeaconBinding beaconBinding) {

        final SwipeDismissBehavior<CardView> swipe = new SwipeDismissBehavior();
        swipe.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY);
        swipe.setListener(new SwipeDismissBehavior.OnDismissListener() {
            @Override
            public void onDismiss(View view) {
                Log.d(Constants.TAG, "Swipe +");
            }

            @Override
            public void onDragStateChanged(int state) {
            }
        });

    }

    public int getActionCount(String beaconId) {
        ActionBeaconAdapter adapter = mActionAdapters.get(beaconId);
        if (adapter != null) {
            return adapter.getItemCount();
        }
        return 0;
    }

    public void removeBeaconAction(String beaconId, int id) {
        ActionBeaconAdapter adapter = mActionAdapters.get(beaconId);
        if (adapter != null) {
            adapter.removeItemById(id);
        }
    }

    public void addBeaconAction(ActionBeacon newAction) {
        ActionBeaconAdapter adapter = mActionAdapters.get(newAction.getBeaconId());
        if (adapter != null) {
            adapter.addItem(newAction);
        }
    }

    public void updateBeaconAction(ActionBeacon action) {
        ActionBeaconAdapter adapter = mActionAdapters.get(action.getBeaconId());
        if (adapter != null) {
            adapter.addItem(action);
        }
    }

    public IManagedBeacon getBeacon(int position) {
        return (IManagedBeacon) getItem(position);
    }

    public void enableAction(String beaconId, int id, boolean enable) {
        ActionBeaconAdapter adapter = mActionAdapters.get(beaconId);
        if (adapter != null) {
            ActionBeacon action = adapter.getItemById(id);
            if (action != null) {
                action.setIsEnabled(enable);
                adapter.addItem(action);
            }
        }
    }


    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemTrackedBeaconBinding binding;

        public BindingHolder(ItemTrackedBeaconBinding binding) {
            super(binding.cardView);
            this.binding = binding;
        }

        public void setOnLongClickListener(View.OnLongClickListener listener) {
            binding.cardView.setOnLongClickListener(listener);
        }
    }

}
