package com.pallavsaikia.task.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.pallavsaikia.task.R
import com.pallavsaikia.task.pojo.ApiPojo
import com.pallavsaikia.task.pojo.Data
import com.pallavsaikia.task.utilities.*
import com.pallavsaikia.task.viewmodel.ApiViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.unit_recycle_view.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), PageListener {

    private val vm: ApiViewModel by viewModel()
    lateinit var restaurantPaginationAdapter: AbstractAdapterRecycleView
    var currentDateAndTime: Long = 0
    val list = mutableListOf<Data>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /**
         * current time
         */
        currentDateAndTime = Calendar.getInstance().getTimeInMillis()
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycleView.layoutManager = layoutManager
        /***
         * intialize pagination
         */
        PaginationUtils.initPagination(recycleView, layoutManager,
            this@MainActivity)

        initRecycleViewUI()

        if (vm.getList().isEmpty()) {
            /**
             * if list is empty api call
             */
            apiCall(false)
        } else {
            for (i in vm.getList()) {
                restaurantPaginationAdapter.addToList(i)
            }
        }
    }

    private fun initRecycleViewUI() {
        restaurantPaginationAdapter = AbstractAdapterRecycleView(
            list.toMutableList(),
            R.layout.unit_recycle_view
        ) { view, any, i ->
            val data = any as Data
            view.itemText.text = data.itemText
            view.itemHeadLine.text = data.itemHeadline
            /**
             * for  image name
             */
            imageName(view,data)

            /**
             * for image
             */
            view.image.load(data.img) {
                placeholder(
                    ContextCompat.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_image_black_24dp
                    )
                )
                error(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_image_black_24dp))
            }
            /**
             * for trending
             */
            if (data.isTrending) {
                view.isTrending.visible()
            } else {
                view.isTrending.gone()
            }

            /**
             * expiry
             */
            expiry(view,data)
        }
        recycleView.adapter = restaurantPaginationAdapter
    }

    private fun expiry(view: View, data: Data) {
        if (data.expiryDate != null) {
            val calCurr = Calendar.getInstance()
            val dateArray = data.expiryDate.split("T", ignoreCase = true)
            val dateFormat = SimpleDateFormat("dd-mm-yyyy HH:mm:ss")
            val date = dateFormat.parse(dateArray[0] + " " + dateArray[1])
            if (date.time > 0) {

                val day = Calendar.getInstance()
                day.setTime(date)
                view.endsIn.text = "Ending in "+(day.get(Calendar.DAY_OF_MONTH) -(calCurr.get(Calendar.DAY_OF_MONTH))).toString()+" days"

            } else {
                val dateFormat1 = SimpleDateFormat("yyyy-mm-dd HH:mm:ss")
                val date1 = dateFormat1.parse(dateArray[0] + " " + dateArray[1])
                val day = Calendar.getInstance()
                day.setTime(date1)
                view.endsIn.text = "Ending in "+(day.get(Calendar.DAY_OF_MONTH) -(calCurr.get(Calendar.DAY_OF_MONTH))).toString()+" days"
            }
        }
    }

    private fun imageName(view: View, data: Data) {
        val nameList = data.img.split("/")
        val image = nameList.get(nameList.size - 1)
        val image1 = image.split("?")
        view.imageName.text = image1[0].split(".")[0]
    }

    /**
     * api call
     */
    private fun apiCall(isScrolling:Boolean) {
        vm.apiCall(isScrolling).observe(this, Observer {
            if (it.error == null) {
                val res = it.data as ApiPojo
                for (i in res.data) {
                    restaurantPaginationAdapter.addToList(i)
                }
            }
        })
    }

    override fun onPagination(page: Int) {
        apiCall(true)

    }
}
