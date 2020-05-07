package com.pallavsaikia.task.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pallavsaikia.task.R
import kotlinx.android.synthetic.main.sort_bottom_sheet.*


class SortBottomSheetFragment(val isT:Int?=-1, val callBack: ((Int) -> Unit)) : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.sort_bottom_sheet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        when(isT){
            0-> radioGroup.check(R.id.ascending)
            1-> radioGroup.check(R.id.descending)
            -1-> radioGroup.check(-1)
        }
        sortBtn.setOnClickListener {
            dismiss()
            when(radioGroup.checkedRadioButtonId){
                -1 ->  callBack(-1)
                R.id.ascending->  callBack(0)
                R.id.descending->  callBack(1)
            }

        }
    }
}