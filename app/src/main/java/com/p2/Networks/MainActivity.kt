package com.p2.Networks



import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONTokener
import java.net.URL



class MainActivity : AppCompatActivity() {

    lateinit var nameView: TextView
    lateinit var dkView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nameView = findViewById(R.id.name)
        dkView = findViewById(R.id.diameterKM)


        //IO :optimized to perform disk or network I/O outside of the main thread
        CoroutineScope(IO).launch {
            getData()
        }

    }

    private suspend fun getData() {
        withContext(IO) {
            val text = URL("https://roversgame.net/cs3680/planets.json").readText()
            Log.i("CS3680", text)
            //main: used only for interacting with the UI and performing quick work.
            //
            withContext(Main) {
                //parsed the text
                val jsonArray = JSONTokener(text).nextValue() as JSONArray


                //loop inside the array and find the 5th index
                // then loop inside that array to find jupiter
                for (i in 0 until jsonArray.length()) {
                    val planet = jsonArray.getJSONObject(i)
                    val name = planet.getString("name")
                    Log.i("cs32", name)
                    //
                    if (name == "Jupiter") {
                        val satellites = planet.getJSONArray("satellites")
                        val moon = satellites.getJSONObject(2)
                        val moonName = moon.getString("name")
                        val dkMoon = moon.getString("diameterKm")
                        Log.i("cs22", moonName)
                        Log.i("cs22", dkMoon)

                        nameView.text = moonName
                        dkView.text = dkMoon
                    }
                }

            }
        }
    }
}













