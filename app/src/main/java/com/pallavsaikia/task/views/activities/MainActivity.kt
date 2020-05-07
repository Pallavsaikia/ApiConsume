package com.pallavsaikia.task.views.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.pallavsaikia.task.views.fragment.SortBottomSheetFragment
import com.pallavsaikia.task.views.fragment.TrendingBottomSheetFragment
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

        clickListeners()
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
            sort.visible()
            filter.visible()
            if(vm.isTrending){
                val isTrendingList=vm.getList().filter {
                    it.isTrending
                }
                for (i in isTrendingList) {
                    restaurantPaginationAdapter.addToList(i)
                }
            }else{
                for (i in vm.getList()) {
                    restaurantPaginationAdapter.addToList(i)
                }
            }
        }
    }

    private fun clickListeners() {

        search.addTextChangedListener(object : TextWatcher{

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                restaurantPaginationAdapter.clearList()
                val searchBList=vm.getList().filter {
                    it.itemHeadline.contains(s.toString(),true)
                }

                if(vm.isTrending){
                    val searchList=searchBList.filter {
                        it.isTrending
                    }
                    populateRecycleView(searchList)
                }else{
                    populateRecycleView(searchBList)
                }

            }

        })

        sort.setOnClickListener {
            val fragment = SortBottomSheetFragment(vm.ascending){

                restaurantPaginationAdapter.clearList()
                vm.ascending=it
                if(it==0){
                    vm.addDateTime(true)
                    val sort=vm.getList().sortedBy {
                        it.time
                    }

                    for(i in sort) {
                        Log.d("asdsa", "${i.time}")
                    }
                    populateRecycleView(sort)
                }else if(it==1){
                    vm.addDateTime(false)
                    val sort=vm.getList().sortedByDescending {
                        it.time
                    }
                    populateRecycleView(sort)
                }else{
                    populateRecycleView(vm.getList())
                }
            }
            fragment.show(supportFragmentManager, "mesnu")
        }
        filter.setOnClickListener {
            val fragment = TrendingBottomSheetFragment(vm.isTrending)

            {
                vm.isTrending=it
                restaurantPaginationAdapter.clearList()
                if(it){
                    val isTrendingList=vm.getList().filter {
                        it.isTrending
                    }
                    for (i in isTrendingList) {
                        restaurantPaginationAdapter.addToList(i)
                    }
                }else{
                    for (i in vm.getList()) {
                        restaurantPaginationAdapter.addToList(i)
                    }
                }
            }

            fragment.show(supportFragmentManager, "menu")
        }
    }

    private fun populateRecycleView(list:List<Data>) {
        for (i in list) {
            restaurantPaginationAdapter.addToList(i)
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
        }else{
            view.endsIn.text = ""
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
            sort.visible()
            filter.visible()
            if (it.error == null) {
                val res = it.data as ApiPojo
                if(vm.isTrending){
                    val isTrendingList=res.data.filter {
                        it.isTrending
                    }
                    for (i in isTrendingList) {
                        restaurantPaginationAdapter.addToList(i)
                    }
                }else{
                    for (i in res.data) {
                        restaurantPaginationAdapter.addToList(i)
                    }
                }

            }
        })
    }

    override fun onPagination(page: Int) {
        apiCall(true)

    }
}
