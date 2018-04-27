

package com.beacon.shopping.assistant.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.beacon.shopping.assistant.R;
import com.beacon.shopping.assistant.databinding.ItemActionBeaconBinding;
import com.beacon.shopping.assistant.model.ActionBeacon;
import com.beacon.shopping.assistant.ui.fragment.TrackedBeaconsFragment;
import com.beacon.shopping.assistant.viewModel.ActionBeaconViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lahiru on 19/02/18.
 */
public class ActionBeaconAdapter extends RecyclerView.Adapter<ActionBeaconAdapter.BindingHolder> {
    private List<ActionBeacon> mItemsList;
    private TrackedBeaconsFragment mFragment;

    public ActionBeaconAdapter(TrackedBeaconsFragment fragment) {
        mFragment = fragment;
        mItemsList = new ArrayList<>();
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemActionBeaconBinding beaconBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_action_beacon,
                parent,
                false);
        return new BindingHolder(beaconBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ItemActionBeaconBinding itemBinding = holder.binding;
        itemBinding.setViewModel(new ActionBeaconViewModel(mFragment, mItemsList.get(position)));
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    public void setItems(List<ActionBeacon> itemsList) {
        this.mItemsList = itemsList;
        notifyDataSetChanged();
    }

    public void addItem(ActionBeacon itemsList) {
        if (!this.mItemsList.contains(itemsList)) {
            this.mItemsList.add(itemsList);
            notifyItemInserted(this.mItemsList.size() - 1);
        } else {
            this.mItemsList.set(mItemsList.indexOf(itemsList), itemsList);
            notifyItemChanged(this.mItemsList.indexOf(itemsList));
        }
    }

    public void removeItemById(int id) {
        for (ActionBeacon action : mItemsList) {
            if (action.getId() == id) {
                mItemsList.remove(action);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public ActionBeacon getItemById(int id) {
        for (ActionBeacon action : mItemsList) {
            if (action.getId() == id) {
                return action;
            }
        }
        return null;
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemActionBeaconBinding binding;

        public BindingHolder(ItemActionBeaconBinding binding) {
            super(binding.contentView);
            this.binding = binding;
        }
    }

}