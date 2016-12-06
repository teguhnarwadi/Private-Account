package com.narwadi.saveyouraccount;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.narwadi.saveyouraccount.base.RecycleBaseAdapter;
import com.narwadi.saveyouraccount.helper.AccountHelper;
import com.narwadi.saveyouraccount.model.AccountModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private ArrayList<AccountModel> list = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecycleBaseAdapter recycleBaseAdapter;

    private Paint p = new Paint();
    private AccountModel accountEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initViews();

    }

    private void initBottomSheet(AccountModel item) {
        accountEdit = item;

        // use bottom sheet dialog
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.dialog_bottom_sheet, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();

        // add OnClickListeners to the buttons of the BottomSheet
        // take the items from the sheetView we defined
        LinearLayout share = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_share);
        // add the listeners
        share.setOnClickListener(this);

    }

    private void initDialog(AccountModel item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_view, null);
        alertDialog.setView(view);

        TextView name = (TextView) view.findViewById(R.id.textView_name);
        TextView email = (TextView) view.findViewById(R.id.textView_email);
        TextView password = (TextView) view.findViewById(R.id.textView_password);
        name.setText(item.getName());
        email.setText(item.getEmail());
        password.setText(item.getPassword());

        alertDialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Show dialog
        alertDialog.show();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageButton ibUser = (ImageButton) toolbar.findViewById(R.id.imageButton_user);
        ibUser.setOnClickListener(this);
        setSupportActionBar(toolbar);
    }

    private void initViews() {
        // Onclick floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        // initialize recycle view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // get account data in a arraylist
        AccountHelper account = new AccountHelper(MainActivity.this);
        list = account.findAllAccount();

        // add list account to adapter
        recycleBaseAdapter = new RecycleBaseAdapter(MainActivity.this, list);
        mRecyclerView.setAdapter(recycleBaseAdapter);
        recycleBaseAdapter.notifyDataSetChanged();
        // set recyvle view item click
        recycleClick();

        // initialize swipe
        initSwipe();

    }

    private void recycleClick() {
        // set on item click
        recycleBaseAdapter.setOnItemClickListener(new RecycleBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AccountModel item) {
                // initialze dialog for view account
                initDialog(item);
            }
        });

        // set on item long click
        recycleBaseAdapter.setOnItemLongClickListener(new RecycleBaseAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(AccountModel item) {
                // The View with the BottomSheetBehavior
                initBottomSheet(item);
            }
        });
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    final View viewSnakeBar = viewHolder.itemView;
                    Snackbar.make(viewSnakeBar, "DELETE", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    //remove from db
                    AccountHelper account = new AccountHelper(MainActivity.this);
                    account.delete(list.get(position).getId());
                    recycleBaseAdapter.removeItem(position);
                } else {
                    accountEdit = list.get(position);
                    // Creating Bundle object
                    Bundle bundle = new Bundle();
                    // Storing data into bundle
                    bundle.putSerializable("account", accountEdit);
                    // Start intent
                    Intent intent2 = new Intent(MainActivity.this, EditActivity.class);
                    // Storing bundle object into intent
                    intent2.putExtras(bundle);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent2);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void showLog(String s) {
        Log.d(TAG, s);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.fragment_history_bottom_sheet_share:
                // set content from account
                String content = accountEdit.getName() + "\n" + accountEdit.getEmail() + "\n" + accountEdit.getPassword();
                // start intent share text content
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, content);
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
                break;
            case R.id.imageButton_user:
                Intent intent3 = new Intent(MainActivity.this, UserActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent3);
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        dialogExit();
    }

    private void dialogExit() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View viewExit = getLayoutInflater().inflate(R.layout.dialog_exit, null);
        alert.setView(viewExit);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }
}
