package name.tutorial1.multithreadingtest.ui.gallery;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import name.tutorial1.multithreadingtest.R;
import name.tutorial1.multithreadingtest.ui.home.HomeFragment;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    private static final String TAG = "homeFrag";
    private Button buttonStartThread;
    private Button buttonStopThread;
    private volatile boolean stopThread = false;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);

        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        buttonStartThread = root.findViewById(R.id.button_start_thread);
        buttonStopThread = root.findViewById(R.id.button_stop_thread);


        buttonStartThread.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startThread(view);
            }
        });

        buttonStopThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopThread(view);
            }
        });


        return root;
    }




    public void stopThread(View view) {
        stopThread = true;
    }

    //    multi threads
    public void startThread(View view) {
        stopThread = false;
        GalleryFragment.ExampleRunnable runnable = new GalleryFragment.ExampleRunnable(10);
        new Thread(runnable).start();
        /*
        ExampleThread thread = new ExampleThread(10);
        thread.start();
        */
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                //work
            }
        }).start();
        */
    }

    public class ExampleRunnable implements Runnable {
        int seconds;

        ExampleRunnable(int seconds) {
            this.seconds = seconds;
        }

        @Override
        public void run() {
            for (int i = 0; i < seconds; i++) {
                if (stopThread)
                    return;
                if (i == 5) {

                    Handler threadHandler = new Handler(Looper.getMainLooper());
                    threadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            buttonStartThread.setText("50%");
                        }
                    });

/*
                    buttonStartThread.post(new Runnable() {
                        @Override
                        public void run() {
                            buttonStartThread.setText("50%");
                        }
                    });
                    */
                }
                Log.d(TAG, "startThread: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}