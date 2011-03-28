package edu.rosehulman.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import edu.rosehulman.example.highscores.R;

public class ShowScores extends Activity {
	
	private static final int DIALOG_ID = 1;
	
	private List<Score> mScores;
	private ListView mScoreListView;
	private ArrayAdapter<Score> mScoreAdapter;
	
	private ScoreDbAdapter mDbAdapter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mDbAdapter = new ScoreDbAdapter(this);
		mDbAdapter.open();
		
		mScoreListView = (ListView) findViewById(R.id.scores_list);
		mScores = new ArrayList<Score>();
		
		Score s = new Score();
		s.setName("Eric");
		s.setScore(5);
		mScores.addAll(mDbAdapter.getAllScores());
		mScores.add(s);
		
		mScoreAdapter = new ArrayAdapter<Score>(this, android.R.layout.simple_list_item_1, mScores);
		mScoreListView.setAdapter(mScoreAdapter);
		mScoreListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				removeScore(mScores.get(position));
				return false;
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDbAdapter.close();
	}
	
	private void addScore(Score s) {
		Toast.makeText(this, "Adding score: " + s, Toast.LENGTH_SHORT);
		s = mDbAdapter.addScore(s);
		mScores.add(s);
		mScoreAdapter.notifyDataSetChanged();
	}
	
	private void removeScore(Score s) {
		Toast.makeText(this, "Removing score: " + s, Toast.LENGTH_SHORT);
		mDbAdapter.removeScore(s);
		mScores.remove(s);
		mScoreAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);
		final Dialog dialog = new Dialog(this);
		if (id == DIALOG_ID) {
			dialog.setContentView(R.layout.add_dialog);
			dialog.setTitle(R.string.add_score);
			
			final EditText nameText = (EditText) dialog.findViewById(R.id.name_entry);
			final EditText scoreText = (EditText) dialog.findViewById(R.id.score_entry);
			final Button addButton = (Button) dialog.findViewById(R.id.add_score_button);
			final Button cancelButton = (Button) dialog.findViewById(R.id.cancel_score_button);
			
			addButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Score s = new Score();
					s.setName(nameText.getText().toString());
					try {
						s.setScore(Integer.parseInt(scoreText.getText().toString()));
					} catch (NumberFormatException e) {
						s.setScore(0);
					}
					addScore(s);
					dialog.dismiss();
				}
			});
			
			cancelButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}
		return dialog;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		if (item.getItemId() == R.id.menu_add) {
			showDialog(DIALOG_ID);
		}
		return false;
	}
}