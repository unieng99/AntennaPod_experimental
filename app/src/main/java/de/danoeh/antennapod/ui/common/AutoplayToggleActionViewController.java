package de.danoeh.antennapod.ui.common;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;

import de.danoeh.antennapod.R;
import de.danoeh.antennapod.storage.preferences.UserPreferences;
import de.danoeh.antennapod.activity.MainActivity;

/**
 * Toolbar action view controller for toggling continuous playback (follow queue).
 */
public class AutoplayToggleActionViewController {
    private final MenuItem menuItem;
    private final Context context;
    private AppCompatImageView iconView;

    private AutoplayToggleActionViewController(MenuItem menuItem, Context context) {
        this.menuItem = menuItem;
        this.context = context;
    }

    @Nullable
    public static AutoplayToggleActionViewController attach(Menu menu, @IdRes int menuId, Context context) {
        if (menu == null || context == null) {
            return null;
        }
        MenuItem item = menu.findItem(menuId);
        if (item == null) {
            return null;
        }
        AutoplayToggleActionViewController controller = new AutoplayToggleActionViewController(item, context);
        controller.bind();
        return controller;
    }

    private void bind() {
        iconView = new AppCompatImageView(context);
        int size = (int) (context.getResources().getDisplayMetrics().density * 42);
        iconView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
        iconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        iconView.setOnClickListener(v -> toggleFollowQueue());
        menuItem.setActionView(iconView);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        syncState();
    }

    private void toggleFollowQueue() {
        boolean enabled = !UserPreferences.isFollowQueue();
        UserPreferences.setFollowQueue(enabled);
        applyState(enabled);
        if (context instanceof MainActivity) {
            ((MainActivity) context).refreshDevStateHeader();
        }
    }

    public void syncState() {
        applyState(UserPreferences.isFollowQueue());
    }

    private void applyState(boolean enabled) {
        if (iconView == null) {
            return;
        }
        int drawableRes = enabled ? R.drawable.ic_shortcut_autoplay_on : R.drawable.ic_shortcut_autoplay_off;
        iconView.setImageDrawable(AppCompatResources.getDrawable(context, drawableRes));
        iconView.setContentDescription(context.getString(
                enabled ? R.string.autoplay_toggle_on : R.string.autoplay_toggle_off));
    }

    public void clear() {
        if (iconView != null) {
            iconView.setOnClickListener(null);
        }
    }
}
