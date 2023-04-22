package com.bid.app.model;


import android.os.AsyncTask;
import android.util.Log;

import com.bid.app.model.view.AvailableRoute;
import com.bid.app.util.DirectionsJSONParser;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetDirectionRoute {
    public static int ROUTE_PATH_TASK = 0;

    public interface DirectionResultListener {
        public void onLoad(ArrayList<LatLng> directionResult);
    }

    DirectionResultListener directionResultListener;

    public GetDirectionRoute(DirectionResultListener directionResultListener) {
        this.directionResultListener = directionResultListener;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        int task_type;
        public DownloadTask(int task_type) {
            this.task_type = task_type;
        }
        private class MapParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

            // Parsing the data in non-ui thread
            @Override
            protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

                JSONObject jObject;
                List<List<HashMap<String, String>>> routes = null;

                try {
                    jObject = new JSONObject(jsonData[0]);
                    DirectionsJSONParser parser = new DirectionsJSONParser();

                    routes = parser.parse(jObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return routes;
            }

            @Override
            protected void onPostExecute(List<List<HashMap<String, String>>> result) {
                ArrayList<LatLng> point_array = new ArrayList<>();
                for (int i = 0; i < 1 && result.size() > 0; i++) { // length is result.size()
                    List<HashMap<String, String>> path = result.get(i);
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        point_array.add(position);
                    }
                }

                directionResultListener.onLoad(point_array);
            }
        }

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            MapParserTask parserTask = new MapParserTask();
            parserTask.execute(result);
        }
    }


    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);


            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public void getRoute(AvailableRoute availableRoute, String mode) {
//        String sourceLocation = availableRoute.getBusStops().get(0).getBusStop();
//        String destinationLocation =  waypointsRoute.getBusStops().get( waypointsRoute.getBusStops().size() - 1).getBusStop();
//        String waypoints = "";
//        for(int i = 1; i <  waypointsRoute.getBusStops().size() - 1; i++) {
//            if(i>1) waypoints += "|";
//            waypoints += waypointsRoute.getBusStops().get(i);
//        }
//        String url =  "https://maps.googleapis.com/maps/api/directions/json?origin=" + sourceLocation +"&destination=" + destinationLocation + "&waypoints=" + waypoints + "&mode=" + mode +"&key=12345678900123123123";
//
//        DownloadTask downloadTask = new DownloadTask(ROUTE_PATH_TASK);
//
//        // Start downloading json data from Google Directions API
//        downloadTask.execute(url);
    }
    public void getRoute(List<String> waypointsRoute, String mode) {
        String sourceLocation = waypointsRoute.get(0);
        String destinationLocation =  waypointsRoute.get( waypointsRoute.size() - 1);
        String waypoints = "";
        for(int i = 1; i <  waypointsRoute.size() - 1; i++) {
            if(i>1) waypoints += "|";
            waypoints += waypointsRoute.get(i);
        }
        String url =  "https://maps.googleapis.com/maps/api/directions/json?origin=" + sourceLocation +"&destination=" + destinationLocation;
        if(waypoints.length() > 0 ) url += "&waypoints=" + waypoints ;
        url +="&mode=" + mode + "&key=12345678900123123123";

        DownloadTask downloadTask = new DownloadTask(ROUTE_PATH_TASK);

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }
}

