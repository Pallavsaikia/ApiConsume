package com.pallavsaikia.task.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pallavsaikia.task.R
import kotlinx.android.synthetic.main.trending_bottom_sheet.*


class TrendingBottomSheetFragment(val isT:Boolean,val callBack: ((Boolean) -> Unit)) : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.trending_bottom_sheet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        isTrending.isChecked=isT

        isTrendingBtn.setOnClickListener {
            dismiss()
            callBack(isTrending.isChecked)
        }
    }
}