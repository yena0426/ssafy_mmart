package com.example.mmart

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.navOptions
import coil.compose.AsyncImage
import com.example.mmart.ui.theme.*
import kotlinx.coroutines.async

//import com.unity3d.player.UnityPlayerActivity
lateinit var getResult: ActivityResultLauncher<Intent>
var pageCode by mutableStateOf(0) // <- this line
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                pageCode= it.data?.getStringExtra("pageCode")?.toInt() ?: 0
            }
        }

        setContent {
            MmARtTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "main") {
                    // 로그인
                    composable(route = "login") {
                        Login(navController)
                    }
                    // 회원 가입
                    composable(route = "signUp") {
                        SignUp(navController)
                    }
                    // 메인 화면
                    composable(route = "main") {
                        Main(navController)
                    }
                    //카테고리 별 상품 보기
                    composable(
                        route = "category/{categoryIdx}",
                        arguments = listOf(navArgument("categoryIdx") { type = NavType.IntType })
                    ) { backStackEntry ->
                        Category(navController, backStackEntry.arguments!!.getInt("categoryIdx"))
                    }
                    // 바코드 스캔
                    composable(route = "barcodeScan") {
                        BarcodeScan(navController)
                    }
                    // 장볼구니
                    composable(route = "getCart") {
                        GetCart(navController)
                    }
                    // 장봤구니
                    composable(route = "gotCart") {
                        GotCart(navController)
                    }
                    // 마이페이지
                    composable(route = "myPage") {
                        MyPage(navController)
                    }
                    // 유니티 테스트
                    composable(route = "unity"){
                        UnityFragment()

                    }
                    // 상품 상세
                    composable(
                        route = "item/{itemIdx}",
                        arguments = listOf(navArgument("itemIdx") { type = NavType.IntType })
                    ) { backStackEntry ->
                        ItemDetail(navController, backStackEntry.arguments?.getInt("itemIdx"))
                    }
                    // 검색 결과
                    composable(
                        route = "search/{searchWord}",
                        arguments = listOf(navArgument("searchWord") { type = NavType.StringType })
                    ) { backStackEntry ->
                        Search(navController, backStackEntry.arguments!!.getString("searchWord")!!)
                    }
                    // 마이페이지 - 결제 내역 조회
                    composable(route = "payment") {
                        Payment(navController)
                    }
                    // 마이페이지 - 결제 내역 조회 - 상세 조회
                    composable(
                        route = "payment/{paymentIdx}",
                        arguments = listOf(navArgument("paymentIdx") { type = NavType.IntType })
                    ) { backStackEntry ->
                        PaymentDetail(navController, backStackEntry.arguments!!.getInt("paymentIdx"))
                    }
                    // 리뷰 작성 및 수정
                    composable(
                        route = "reviewSave/{paymentDetailIdx}/{reviewIdx}",
                        arguments = listOf(
                            navArgument("paymentDetailIdx") { type = NavType.IntType },
                            navArgument("reviewIdx") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val paymentDetailIdx = backStackEntry.arguments!!.getInt("paymentDetailIdx")
                        val reviewIdx = backStackEntry.arguments!!.getInt("reviewIdx")
                        ReviewSave(navController, paymentDetailIdx, reviewIdx)
                    }
                    // 마이페이지 - 리뷰 내역 조회
                    composable(route = "review") {
                        Review(navController)
                    }
                }

                // 로그인 안됐을 시 로그인
                if (userId == 0) {
                    navController.navigate("login"){
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            }
        }
    }
}

// 유저 아이디
var userId = 0

// 뒤로 가기 두 번 눌렀을 때 앱 종료
@Composable
fun BackOnPressed() {
    val context = LocalContext.current
    var backPressedTime = 0L

    BackHandler() {
        if(System.currentTimeMillis() - backPressedTime >= 1500L) {
            backPressedTime = System.currentTimeMillis()
            Toast.makeText(context, "한 번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else {
            // 앱 종료
            (context as Activity).finish()
        }

    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter") // Scaffold의 padding value 사용 안 할 때
@Composable
fun Main(navController: NavController) {

    var isLoading1: Boolean by remember { mutableStateOf(true) }
    var isLoading2: Boolean by remember { mutableStateOf(true) }
    var isLoading3: Boolean by remember { mutableStateOf(true) }

    // 카테고리 별 상품보기
    fun category(categoryId: Int){
        navController.navigate("category/$categoryId")
    }

    BackOnPressed()
    Scaffold(
        modifier = Modifier.background(Vivid_blue),
        content = {
            // 배경 이미지
            Image(
                painter = painterResource(R.drawable.bg),
                modifier = Modifier.fillMaxSize(),
                contentDescription = "배경",
                contentScale = ContentScale.FillWidth
            )

            Column() {
                // 상단 로고, 지점 표시
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween

                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "로고",
                        modifier = Modifier
                            .width(120.dp)
                            .height(40.dp)
                    )
                    Image(
                        painter = painterResource(R.drawable.place),
                        contentDescription = "지점",
                        modifier = Modifier
                            .width(80.dp)
                            .height(30.dp)
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))

                // 검색
                searchBar(navController)

                // 카테고리, 최근/자주 구매
                Column(
                    modifier = Modifier
                        .padding(20.dp, 10.dp, 20.dp, 110.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    // 카테고리 부분
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(top = 20.dp, bottom = 10.dp)
                            .background(Light_yellow)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.SpaceBetween

                        ) {
                            LazyRow(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp)
                            ) {
                                items(4) { item ->
                                    fun imageResource(num: Int): Int {
                                        return when (num) {
                                            0 -> R.drawable.category_1
                                            1 -> R.drawable.category_2
                                            2 -> R.drawable.category_3
                                            3 -> R.drawable.category_4
                                            else -> R.drawable.category_1
                                        }
                                    }

                                    fun description(num: Int): String {
                                        return when (num) {
                                            0 -> "가공식품"
                                            1 -> "신선식품"
                                            2 -> "일상용품"
                                            3 -> "의약품"
                                            else -> "가공식품"
                                        }
                                    }
                                    Column(
                                        modifier = Modifier
                                            .clickable { category(item + 1) },
                                        horizontalAlignment = Alignment.CenterHorizontally

                                    ) {
                                        Image(
                                            painter = painterResource(imageResource(item)),
                                            contentDescription = description(item),
                                            modifier = Modifier
                                                .size(50.dp)
                                        )
                                        Text(description(item), modifier = Modifier.padding(top=5.dp))
                                    }
                                }
                            }

                            LazyRow(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp)
                            ) {
                                items(4) { item ->
                                    fun imageResource(num: Int): Int {
                                        return when (num) {
                                            0 -> R.drawable.category_5
                                            1 -> R.drawable.category_6
                                            2 -> R.drawable.category_7
                                            3 -> R.drawable.category_8
                                            else -> R.drawable.category_1
                                        }
                                    }

                                    fun description(num: Int): String {
                                        return when (num) {
                                            0 -> "교육용품"
                                            1 -> "디지털"
                                            2 -> "인테리어"
                                            3 -> "스포츠"
                                            else -> "가공식품"
                                        }
                                    }
                                    Column(
                                        modifier = Modifier
                                            .clickable { category(item + 5) },
                                        horizontalAlignment = Alignment.CenterHorizontally

                                    ) {
                                        Image(
                                            painter = painterResource(imageResource(item)),
                                            contentDescription = description(item),
                                            modifier = Modifier
                                                .size(50.dp)
                                        )
                                        Text(description(item), modifier = Modifier.padding(top=5.dp))
                                    }
                                }
                            }
                        }
                    }

                    val api = APIS.create()
                    val coroutineScope = rememberCoroutineScope()

                    // 아이템 정보
                    var recentItems: List<ItemInfo>? by remember { mutableStateOf(null) }
                    var frequentItems: List<ItemInfo>? by remember { mutableStateOf(null) }
                    var discountItems: List<ItemInfo>? by remember { mutableStateOf(null) }

                    LaunchedEffect(true) {
                        try {
                            recentItems = coroutineScope.async { api.getRecentItems(userId) }.await().result
                            frequentItems = coroutineScope.async { api.getFrequentItems(userId) }.await().result
                            discountItems = coroutineScope.async { api.getDiscountItems(userId) }.await().result
                        } catch (e: Exception){
                            println("메인페이지 아이템 조회 에러----------")
                            e.printStackTrace()
                            println("---------------------------------")
                        }
                    }

                    // 최근 구매
                    Text("최근 구매 상품", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(top=20.dp))
                    if(recentItems!=null){
                        if(recentItems!!.isEmpty()){
                            isLoading1 = false
                            Text("최근 구매 상품이 없습니다", modifier = Modifier
                                .padding(vertical = 50.dp)
                                .fillMaxWidth(), textAlign = TextAlign.Center)
                        } else {
                            LazyRow(){
                                items(recentItems!!){
                                        item ->
                                    Column(
                                        modifier = Modifier
                                            .padding(7.dp)
                                            .width(100.dp)
                                            .clickable { navController.navigate("item/${item.itemIdx}") },
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ){
                                        AsyncImage(
                                            model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${item.thumbnail}",
                                            contentDescription = "상품 썸네일",
                                            onSuccess = {isLoading1 = false},
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .aspectRatio(1f)

                                        )
                                        Text(item.itemName, overflow = TextOverflow.Ellipsis, maxLines = 1, textAlign = TextAlign.Center, fontSize = 12.sp, modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 5.dp))
                                    }
                                }
                            }
                        }
                    }

                    // 자주 구매
                    Text("자주 구매 상품", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(top=20.dp))
                    if(frequentItems!=null){
                        if(frequentItems!!.isEmpty()){
                            isLoading2 = false
                            Text("자주 구매 상품이 없습니다", modifier = Modifier
                                .padding(vertical = 50.dp)
                                .fillMaxWidth(), textAlign = TextAlign.Center)
                        } else {
                            LazyRow(){
                                items(frequentItems!!){
                                        item ->
                                    Column(
                                        modifier = Modifier
                                            .padding(7.dp)
                                            .width(100.dp)
                                            .clickable { navController.navigate("item/${item.itemIdx}") },
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ){
                                        AsyncImage(
                                            model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${item.thumbnail}",
                                            contentDescription = "상품 썸네일",
                                            onSuccess = {isLoading2 = false},
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .aspectRatio(1f)

                                        )
                                        Text(item.itemName, overflow = TextOverflow.Ellipsis, maxLines = 1, textAlign = TextAlign.Center, fontSize = 12.sp, modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 5.dp))
                                    }
                                }
                            }
                        }
                    }

                    // 할인 상품
                    Text("할인 상품", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(top=20.dp))
                    if(discountItems!=null){
                        if(discountItems!!.isEmpty()){
                            isLoading3 = false
                            Text("할인 중인 상품이 없습니다", modifier = Modifier
                                .padding(vertical = 50.dp)
                                .fillMaxWidth(), textAlign = TextAlign.Center)
                        } else {
                            LazyRow(){
                                items(discountItems!!){
                                        item ->
                                    Column(
                                        modifier = Modifier
                                            .padding(7.dp)
                                            .width(100.dp)
                                            .clickable { navController.navigate("item/${item.itemIdx}") },
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ){
                                        AsyncImage(
                                            model = "https://mmart405.s3.ap-northeast-2.amazonaws.com/${item.thumbnail}",
                                            contentDescription = "상품 썸네일",
                                            onSuccess = {isLoading3 = false},
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .aspectRatio(1f)

                                        )
                                        Text(item.itemName, overflow = TextOverflow.Ellipsis, maxLines = 1, textAlign = TextAlign.Center, fontSize = 12.sp, modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 5.dp))
                                    }
                                }
                            }
                        }
                    }

                }
            }
        },
        // 하단바
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Image(painter = painterResource(R.drawable.bottombar_1), contentDescription = "빠른장보기",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .aspectRatio(1f / 1f)
                        .clickable(onClick = { navController.navigate("barcodeScan") })
                )
                Image(painter = painterResource(R.drawable.bottombar_2), contentDescription = "장볼구니",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .aspectRatio(1f / 1f)
                        .clickable(onClick = { navController.navigate("getCart") }))
                Image(painter = painterResource(R.drawable.bottombar_3), contentDescription = "장봤구니",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .aspectRatio(1f / 1f)
                        .clickable(onClick = { navController.navigate("gotCart") }))
                Image(painter = painterResource(R.drawable.bottombar_4), contentDescription = "마이페이지",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .aspectRatio(1f / 1f)
                        .clickable(onClick = { navController.navigate("myPage") }))
            }
        }
    )
    if(isLoading1 || isLoading2 || isLoading3) {
        loadingView()
    }
}