package com.example.team_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.team_project.databinding.FragmentAnalysisBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import android.graphics.Color
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS

class AnalysisFragment : Fragment() {
    private var binding: FragmentAnalysisBinding? = null

    // PieData: 차트에 표시될 데이터 값 객체
    // PieDataSet: PieData에 대한 스타일 및 속성을 설정하는 객체 (색상, 텍스트 크기, 간격 등)
    // PieEntry: 차트의 한 부분에 해당하는 데이터를 나타내는 객체 (크기, 라벨 설정)
    // PieChart: 실제로 화면에 표시되는 PieChart 객체

    private var pieChart: PieChart? = null
    private var pieEntries = ArrayList<PieEntry>()
    private var pieDataSet: PieDataSet? = null
    private var pieData: PieData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalysisBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pieChart = view.findViewById(R.id.pie) // fragment_analysis.xml에서 id가 pie인 view를 가리키는 변수
        setData()
        setColor()
    }

    fun setData() {
        pieChart?.setUsePercentValues(true) // 100% 로 맞춰서 계산

        pieEntries.add(PieEntry(30f, "일식"))
        pieEntries.add(PieEntry(40f, "한식"))
        pieEntries.add(PieEntry(40f, "중식"))
        pieEntries.add(PieEntry(60f, "양식"))
    }

    fun setColor() {
        val colorsItems = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colorsItems.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colorsItems.add(c)
        for (c in COLORFUL_COLORS) colorsItems.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colorsItems.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colorsItems.add(c)
        colorsItems.add(ColorTemplate.getHoloBlue())

        pieDataSet = PieDataSet(pieEntries, "최근 지출 목록").apply {
            colors = colorsItems
            valueTextColor = Color.BLACK
            valueTextSize = 16f
        }

        pieDataSet?.colors = listOf(Color.BLUE, Color.GREEN, Color.RED)
        pieDataSet?.selectionShift
    }
    fun setUpData() {
        pieData = PieData(pieDataSet)
        binding?.pie?.apply {
            data = pieData
            description.isEnabled = false
            isRotationEnabled = false
            centerText = "This is Center"
            setEntryLabelColor(Color.BLACK)
            animateY(1400, Easing.EaseInOutQuad)
            animate()
        }
    }

}