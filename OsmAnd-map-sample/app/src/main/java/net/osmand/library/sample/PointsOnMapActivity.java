package net.osmand.library.sample;

import static net.osmand.plus.utils.InsetsUtils.InsetSide.BOTTOM;
import static net.osmand.plus.utils.InsetsUtils.InsetSide.LEFT;
import static net.osmand.plus.utils.InsetsUtils.InsetSide.RIGHT;
import static net.osmand.plus.utils.InsetsUtils.InsetSide.TOP;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import net.osmand.data.FavouritePoint;
import net.osmand.plus.OsmandApplication;
import net.osmand.plus.activities.OsmandActionBarActivity;
import net.osmand.plus.activities.RestartActivity;
import net.osmand.plus.helpers.AndroidUiHelper;
import net.osmand.plus.utils.AndroidUtils;
import net.osmand.plus.utils.InsetTarget;
import net.osmand.plus.utils.InsetTarget.InsetTargetBuilder;
import net.osmand.plus.utils.InsetsUtils;
import net.osmand.plus.views.MapViewWithLayers;
import net.osmand.plus.views.OsmandMapTileView;

import java.util.ArrayList;
import java.util.List;

public class PointsOnMapActivity extends OsmandActionBarActivity {

	private OsmandApplication app;
	private OsmandMapTileView mapTileView;
	private MapViewWithLayers mapViewWithLayers;
	private CustomPointsLayer customPointsLayer;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_map_activity);
		mapViewWithLayers = findViewById(R.id.map_view_with_layers);

		app = (OsmandApplication) getApplication();

		mapTileView = app.getOsmandMap().getMapView();
		mapTileView.setupRenderingView();

		customPointsLayer = new CustomPointsLayer(this, getFavouritePoints());
		mapTileView.addLayer(customPointsLayer, 5.5f);

		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("Points on map");
		toolbar.setNavigationIcon(AndroidUtils.getNavigationIconResId(app));
		toolbar.setNavigationOnClickListener(v -> onBackPressed());

		CompoundButton openglSwitch = findViewById(R.id.opengl_switch);
		openglSwitch.setChecked(app.getSettings().USE_OPENGL_RENDER.get());
		openglSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
			app.getSettings().USE_OPENGL_RENDER.set(isChecked);
			RestartActivity.doRestart(this);
		});

		//set start location and zoom for map
		mapTileView.setIntZoom(14);
		mapTileView.setLatLon(52.3704312, 4.8904288);
	}

	@Override
	public void updateStatusBarColor() {
		int color = AndroidUtils.getColorFromAttr(this, android.R.attr.colorPrimary);
		if (color != -1) {
			AndroidUiHelper.setStatusBarColor(this, color);
		}
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();

		View root = findViewById(R.id.root);
		InsetTargetBuilder builder = InsetTarget.builder(root)
				.portraitSides(BOTTOM, TOP)
				.landscapeSides(TOP, RIGHT, LEFT);

		InsetsUtils.setWindowInsetsListener(root, (view, windowInsetsCompat)
				-> InsetsUtils.applyPadding(view, windowInsetsCompat, builder.build()), true);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapViewWithLayers.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapViewWithLayers.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapViewWithLayers.onDestroy();
		mapTileView.removeLayer(customPointsLayer);
	}

	private List<FavouritePoint> getFavouritePoints() {
		List<FavouritePoint> points = new ArrayList<>();
		points.add(new FavouritePoint(50.8465565, 4.351697, "Brussel", "cities"));
		points.add(new FavouritePoint(51.5073219, -0.1276474, "London", "cities"));
		points.add(new FavouritePoint(48.8566101, 2.3514992, "Paris", "cities"));
		points.add(new FavouritePoint(47.4983815, 19.0404707, "Budapest", "cities"));
		points.add(new FavouritePoint(55.7506828, 37.6174976, "Moscow", "cities"));
		points.add(new FavouritePoint(39.9059631, 116.391248, "Beijing", "cities"));
		points.add(new FavouritePoint(35.6828378, 139.7589667, "Tokyo", "cities"));
		points.add(new FavouritePoint(38.8949549, -77.0366456, "Washington", "cities"));
		points.add(new FavouritePoint(45.4210328, -75.6900219, "Ottawa", "cities"));
		points.add(new FavouritePoint(8.9710438, -79.5340599, "Panama", "cities"));
		points.add(new FavouritePoint(53.9072394, 27.5863608, "Minsk", "cities"));
		points.add(new FavouritePoint(52.5162303, 13.3777309, "Berlin", "cities"));
		points.add(new FavouritePoint(52.3704312, 4.8904288, "Amsterdam", "cities"));

		return points;
	}
}
