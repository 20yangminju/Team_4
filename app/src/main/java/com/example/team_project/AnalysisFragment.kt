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
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.team_project.viewmodel.AnalysisViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class AnalysisFragment : Fragment() {
    private var binding: FragmentAnalysisBinding? = null

    // PieData: 차트에 표시될 데이터 값 객체
    // PieDataSet: PieData에 대한 스타일 및 속성을 설정하는 객체 (색상, 텍스트 크기, 간격 등)
    // PieEntry: 차트의 한 부분에 해당하는 데이터를 나타내는 객체 (크기, 라벨 설정)
    // PieChart: 실제로 화면에 표시되는 PieChart 객체

    // PieEntry (데이터) -> PieDataSet (그래프 설정) -> PieData (화면에 출력할 데이터 설정)

    private var pieChart: PieChart? = null
    private var pieEntries = ArrayList<PieEntry>()
    private var pieDataSet: PieDataSet? = null
    private var pieData: PieData? = null
    private val label = arrayOf("한식", "중식", "일식", "양식", "Fast food")

    private val typeRef = Firebase.database.getReference("restaurant")

    val viewModel: AnalysisViewModel by activityViewModels()

    // Fragment의 레이아웃 인플레이트 후 반환
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalysisBinding.inflate(inflater, container, false)

        // recycler view 설정
        binding?.recentRestaurants?.layoutManager = LinearLayoutManager(context)
        binding?.recentRestaurants?.adapter = RecentAdapter(viewModel.recent)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // recent에 변화 발생 시 호출
        viewModel.recent.observe(viewLifecycleOwner) {
            // pieChart에 추가할 데이터 설정
            pieChart = view.findViewById(R.id.pie) // fragment_analysis.xml에서 id가 pie인 view를 가리키는 변수
            setData()
            setColor()
            setUpData()

            // recycler view 갱신
            binding?.recentRestaurants?.adapter = RecentAdapter(viewModel.recent)
        }

        arguments?.let {
            val name = it.getString("name").toString()
            val menu = it.getString("menu").toString()
            val price = it.getString("price").toString()
            val url = "https://firebasestorage.googleapis.com/v0/b/team-4-a91c7.appspot.com/o/${name}.png?alt=media"

            // type은 firebase에 직접 접근 (음식점 메뉴 화면에 type 데이터를 갖고 있지 않음)
            typeRef.child(name).child("info").addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val type = snapshot.child("foodtype").value.toString()
                    viewModel.setRecent(RecentRestaurant(name, type, menu, price, url))
                    binding?.recentRestaurants?.adapter?.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        // 분석 데이터 비우기
        binding?.resetButton?.setOnClickListener {
            viewModel.reset()
        }

    }

    // pieChart 설정
    fun setData() {
        pieChart?.setUsePercentValues(true) // % 로 맞춰서 계산

        viewModel.setGraph() // 그래프를 출력하기 전, 현재 viewModel 데이터 갱신

        // pieEntries 배열에 데이터 추가
        pieEntries.clear() // 그래프를 비운 후 추가
        for (i in 1..5) {
            if (viewModel.priceList[i] != 0f) { // 0이면 그래프에 추가 X
                pieEntries.add(PieEntry(viewModel.priceList[i], label[i - 1]))
            }
        }
    }

    // pieChart의 색상 설정
    fun setColor() {
        // pieChart의 색상(Int형)을 저장하는 ArrayList
        val colorsItems = ArrayList<Int>()

        // pieChart에 사용될 다양한 색상 추가 (Template 이용)
        for (c in ColorTemplate.VORDIPLOM_COLORS) colorsItems.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colorsItems.add(c)
        for (c in COLORFUL_COLORS) colorsItems.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colorsItems.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colorsItems.add(c)
        colorsItems.add(ColorTemplate.getHoloBlue()) // 직접 추가

        pieDataSet = PieDataSet(pieEntries, "").apply {
            colors = colorsItems // 위에서 설정한 colortsItem로 색 설정
            valueTextColor = Color.BLACK // 각 섹션의 데이터 Text 색을 검은색으로 설정
            valueTextSize = 16f // 각 섹션의 Text 크기을 16f로 설정
        }
    }

    // 화면에 표시되는 데이터 설정
    fun setUpData() {
        // 설정한 PieDataSet으로 화면에 표시할 PieData 객체 생성
        pieData = PieData(pieDataSet)

        // 퍼센트 포맷터를 사용하여 값 표시 형식 설정
        pieData?.setValueFormatter(PercentFormatter(pieChart))

        // xml 레이아웃 설정
        binding?.pie?.run {
            data = pieData // 차트에 표시될 데이터 설정 (스타일 적용 포함)
            description.isEnabled = false // 차트 설명 추가 속성 (없음)
            isRotationEnabled = true // 차트를 회전할 수 있는지 나타내는 속성
            centerText = "최근 소비 지출 내역" // 차트 중앙에 표시될 문장
            setEntryLabelColor(Color.BLACK) // 각 차트의 라벨 텍스트 색상 설정

            animateY(1400, Easing.EaseInOutQuad) // 차트의 에니메이션 설정
            // durationMills: 1400말리초동안 에니메이션 진행
            // Easing.EaseInPutQuad: 가속 및 감속효과

            animate()
            // 함수 호출시 에니메이션 시작
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}