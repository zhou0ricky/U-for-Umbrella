package com.example.umbrellatartanhacks;


import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothClass;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

public class MainActivity extends Activity {
    String userId = "96254";
    String deviceId = "10020";
    /**
     * Mobile Service Client reference
     */
    private MobileServiceClient mClient;

    /**
     * Mobile Service Table used to access data
     */
    private MobileServiceTable<Users> musertable;
    private MobileServiceTable<Devices> mdevicetable;
    private MobileServiceTable<Stations> mstationtable;
    /**
     * Adapter to sync the items list with the view
     */
    private UsersAdapter userAdapter;
    private DevicesAdapter deviceAdapter;
    private StationsAdapter stationAdapter;
    /**
     * EditText containing the "New To Do" text
     */
    private EditText mTextNewToDo;

    /**
     * Progress spinner to use for table operations
     */
    private ProgressBar mProgressBar;

    private ImageView Image_lock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        try {
            // Create the Mobile Service Client instance, using the provided

            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "https://umbrellatartanhacks.azurewebsites.net",
                    this).withFilter(new ProgressFilter());

            // Extend timeout from default of 10s to 20s
            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });

            // Get the Mobile Service Table instance to use

            musertable = mClient.getTable(Users.class);
            mdevicetable = mClient.getTable(Devices.class);
            mstationtable = mClient.getTable(Stations.class);
            // Offline Sync
            //musertable = mClient.getSyncTable("Users", Users.class);

            //Init local storage
            initLocalStore().get();

//            mTextNewToDo = (EditText) findViewById(R.id.textNewToDo);
//
//            // Create an adapter to bind the items with the view
//            userAdapter = new UsersAdapter(this, R.layout.row_list_to_do);
//            deviceAdapter = new DevicesAdapter(this,R.layout.row_list_to_do);
//            stationAdapter = new StationsAdapter(this,R.layout.row_list_to_do);
//            ListView listViewToDo = (ListView) findViewById(R.id.listViewToDo);
//            listViewToDo.setAdapter(userAdapter);

            // Load the items from the Mobile Service
//            refreshUsersFromTable();
//            refreshDevicesFromTable();
//            refreshStationsFromTable();
        } catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e){
            createAndShowDialog(e, "Error");
        }
        Image_lock = (ImageView) findViewById(R.id.img_lock);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                //your method
                try{
                    final Users item = refreshUsersFromMobileServiceTable();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (item.getIsRenting()) {
                                Image_lock.setImageResource(R.drawable.lock_green);
                            }else{
                                Image_lock.setImageResource(R.drawable.lock_red);
                        }
                    };

                    });}catch (java.lang.InterruptedException e){} catch (java.util.concurrent.ExecutionException e){}
            }
        }, 0, 100);
    }

    /**
     * Initializes the activity menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    /**
     * Select an option from the menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
        }

        return true;
    }

    /**
     * Mark an item as completed
     *
     * @param item
     *            The item to mark
     */
    public void checkItem(final Users item) {
        if (mClient == null) {
            return;
        }

        // Set the item as completed and update it in the table

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    System.out.println(item);
                    checkItemInTable(item);
                } catch (final Exception e) {
                    System.out.println("nbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };

        runAsyncTask(task);

    }

    public void checkItem(final Devices item) {
        if (mClient == null) {
            return;
        }

        // Set the item as completed and update it in the table

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    checkItemInTable(item);
                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };

        runAsyncTask(task);

    }

    public void checkItem(final Stations item) {
        if (mClient == null) {
            return;
        }

        // Set the item as completed and update it in the table
        item.setComplete(true);

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    checkItemInTable(item);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (item.isComplete()) {
                                stationAdapter.remove(item);
                            }
                        }
                    });
                } catch (final Exception e) {
                    System.out.println("ddddddddddddddddddddddddddddddddddddddddd");
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };

        runAsyncTask(task);

    }
    /**
     * Mark an item as completed in the Mobile Service Table
     *
     * @param item
     *            The item to mark
     */
    public void checkItemInTable(Users item) throws ExecutionException, InterruptedException {
        musertable.update(item).get();
    }

    public void checkItemInTable(Devices item) throws ExecutionException, InterruptedException {
        mdevicetable.update(item).get();
    }

    public void checkItemInTable(Stations item) throws ExecutionException, InterruptedException {
        mstationtable.update(item).get();
    }

    /**
     * Add a new item
     *
     * @param view
     *            The view that originated the call
     */
    public void addUser(View view) {
        if (mClient == null) {
            return;
        }

        // Create a new item
        final Users item = new Users("Yeet",userId);

        item.setName(mTextNewToDo.getText().toString());

        // Insert the new item
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    System.out.println("a");
                    final Users entity = addUserInTable(item);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                } catch (final Exception e) {
                    System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                    createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };

        runAsyncTask(task);

        mTextNewToDo.setText("");
    }

    public void addDevice(View view) {
        if (mClient == null) {
            return;
        }

        // Create a new item
        final Devices item = new Devices();

        item.setId(mTextNewToDo.getText().toString());

        // Insert the new item
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    System.out.println("a");
                    final Devices entity = addDeviceInTable(item);
                } catch (final Exception e) {
                    System.out.println("ffffffffffffffffffffffffffffffffffffffffffff");
                    createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };

        runAsyncTask(task);

        mTextNewToDo.setText("");
    }

    public void addStation(View view) {
        if (mClient == null) {
            return;
        }

        // Create a new item
        final Stations item = new Stations();

        item.setId(mTextNewToDo.getText().toString());
        item.setComplete(false);

        // Insert the new item
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    System.out.println("a");
                    final Stations entity = addStationInTable(item);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!entity.isComplete()){
                                stationAdapter.add(entity);
                            }
                        }
                    });
                } catch (final Exception e) {
                    System.out.println("ggggggggggggggggggggggggggggggggggggggggggggg");
                    createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };

        runAsyncTask(task);

        mTextNewToDo.setText("");
    }
    /**
     * Add an item to the Mobile Service Table
     *
     * @param item
     *            The item to Add
     */
    public Users addUserInTable(Users item) throws ExecutionException, InterruptedException {
        Users entity = musertable.insert(item).get();
        return entity;
    }

    public Devices addDeviceInTable(Devices item) throws ExecutionException, InterruptedException {
        Devices entity = mdevicetable.insert(item).get();
        return entity;
    }

    public Stations addStationInTable(Stations item) throws ExecutionException, InterruptedException {
        Stations entity = mstationtable.insert(item).get();
        return entity;
    }

    /**
     * Refresh the list with the items in the Table
     */
    private void refreshUsersFromTable() {

        // Get the items that weren't marked as completed and add them in the
        // adapter

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    final Users results = refreshUsersFromMobileServiceTable();

                    //Offline Sync
                    //final List<Users> results = refreshItemsFromMobileServiceTableSyncTable();

                } catch (final Exception e){
                    System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };

        runAsyncTask(task);
    }

    /**
     * Refresh the list with the items in the Mobile Service Table
     */

    private Users refreshUsersFromMobileServiceTable() throws ExecutionException, InterruptedException {
        return musertable.lookUp(userId).get();
    }


    private List<Stations> refreshStationsFromMobileServiceTable() throws ExecutionException, InterruptedException {
        return mstationtable.where().field("complete").
                eq(val(false)).execute().get();
    }

    //Offline Sync
    /**
     * Refresh the list with the items in the Mobile Service Sync Table
     */
    /*private List<Users> refreshItemsFromMobileServiceTableSyncTable() throws ExecutionException, InterruptedException {
        //sync the data
        sync().get();
        Query query = QueryOperations.field("complete").
                eq(val(false));
        return musertable.read(query).get();
    }*/

    /**
     * Initialize local storage
     * @return
     * @throws MobileServiceLocalStoreException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private AsyncTask<Void, Void, Void> initLocalStore() throws MobileServiceLocalStoreException, ExecutionException, InterruptedException {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    MobileServiceSyncContext syncContext = mClient.getSyncContext();

                    if (syncContext.isInitialized())
                        return null;

                    SQLiteLocalStore localStore = new SQLiteLocalStore(mClient.getContext(), "OfflineStore", null, 1);

                    Map<String, ColumnDataType> utableDefinition = new HashMap<String, ColumnDataType>();
                    utableDefinition.put("id", ColumnDataType.String);
                    utableDefinition.put("name", ColumnDataType.String);
                    utableDefinition.put("rentTime", ColumnDataType.Integer);
                    utableDefinition.put("lastUsed", ColumnDataType.String);
                    utableDefinition.put("isRenting", ColumnDataType.Boolean);
                    utableDefinition.put("rentLocX", ColumnDataType.Boolean);
                    utableDefinition.put("rentLocY", ColumnDataType.Boolean);
                    SimpleSyncHandler handler = new SimpleSyncHandler();

                    syncContext.initialize(localStore, handler).get();
                    Map<String, ColumnDataType> dtableDefinition = new HashMap<String, ColumnDataType>();
                    dtableDefinition.put("id", ColumnDataType.String);
                    dtableDefinition.put("size", ColumnDataType.String);
                    dtableDefinition.put("isRented", ColumnDataType.Boolean);
                    localStore.defineTable("Devices", dtableDefinition);

                    Map<String, ColumnDataType> stableDefinition = new HashMap<String, ColumnDataType>();
                    stableDefinition.put("id", ColumnDataType.String);
                    stableDefinition.put("locX", ColumnDataType.Real);
                    stableDefinition.put("locY", ColumnDataType.Real);
                    stableDefinition.put("numSmall", ColumnDataType.Integer);
                    stableDefinition.put("numLarge", ColumnDataType.Integer);
                    localStore.defineTable("Stations", stableDefinition);


                    handler = new SimpleSyncHandler();

                    syncContext.initialize(localStore, handler).get();

                } catch (final Exception e) {
                    System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };

        return runAsyncTask(task);
    }

    //Offline Sync
    /**
     * Sync the current context and the Mobile Service Sync Table
     * @return
     */
    /*
    private AsyncTask<Void, Void, Void> sync() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    MobileServiceSyncContext syncContext = mClient.getSyncContext();
                    syncContext.push().get();
                    musertable.pull(null).get();
                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };
        return runAsyncTask(task);
    }
    */

    /**
     * Creates a dialog and shows it
     *
     * @param exception
     *            The exception to show in the dialog
     * @param title
     *            The dialog title
     */
    private void createAndShowDialogFromTask(final Exception exception, String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("llllllllllllllllllllllllllllllllllllllllllllllllllllllll");
                createAndShowDialog(exception, "Error");
            }
        });
    }


    /**
     * Creates a dialog and shows it
     *
     * @param exception
     *            The exception to show in the dialog
     * @param title
     *            The dialog title
     */
    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }

    /**
     * Creates a dialog and shows it
     *
     * @param message
     *            The dialog message
     * @param title
     *            The dialog title
     */
    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    /**
     * Run an ASync task on the corresponding executor
     * @param task
     * @return
     */
    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    private class ProgressFilter implements ServiceFilter {

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback) {

            final SettableFuture<ServiceFilterResponse> resultFuture = SettableFuture.create();


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
            });

            ListenableFuture<ServiceFilterResponse> future = nextServiceFilterCallback.onNext(request);

            Futures.addCallback(future, new FutureCallback<ServiceFilterResponse>() {
                @Override
                public void onFailure(Throwable e) {
                    resultFuture.setException(e);
                }

                @Override
                public void onSuccess(ServiceFilterResponse response) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
                        }
                    });

                    resultFuture.set(response);
                }
            });

            return resultFuture;
        }
    }
}