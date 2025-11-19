Which API did you choose and why?
  -I chose the OpenWeatherMap API because it provides real-time data, stored in a JSON format. 
  It makes parsing and fetching live data easy.

How did you implement data fetching and JSON parsing?
  -I used Retrofit to handle the network connectivity and API calls. I created three files:
  WeatherApi for creating endpoints and query parameters
  RetrofitClient to configure Retrofit 
  WeatherResponse to map the JSON response into kotlin objects

  When user enters a data, the app sends a request to OpenWeatherMap. It receives the response in JSON, parses it 
  and displays it to the App's TextView.

What challenges did you face when handling errors or slow connections?
  -Not knowing that I need to declare internet permission in the manifest gave me alot of trouble. It gave me errors like
  SecurityException and my app shutting down when I press the "Get Weather" button. I also dealt with network timeout errors.

How would you improve your app's UI performance in future versions?
  -I would like to acknowledge to first declare my internet connectivity and permission access to the manifest whenever I will deal with backend
  processes. Maybe add some more visuals like weather icons and language support.

  
