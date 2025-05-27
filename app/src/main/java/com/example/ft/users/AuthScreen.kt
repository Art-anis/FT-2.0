package com.example.ft.users

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ft.R
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

//экран авторизации
@Composable
fun AuthScreen(
    firstLaunch: Boolean, //флаг первого запуска
    navigateToSearch: () -> Unit //переход после успешной регистрации/входа
) {
    //viewmodel
    val viewmodel = koinInject<AuthViewModel>()
    
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 32.dp)) { 

        //вкладки
        val tabItems = listOf(stringResource(R.string.sign_up), stringResource(R.string.sign_in))

        //индекс выбранной вкладки
        var selectedTabIndex by rememberSaveable { mutableIntStateOf(if (firstLaunch) 0 else 1) }

        //состояние пейджера
        val pagerState = rememberPagerState { tabItems.size }

        //менеджер фокуса для текстовых полей
        val focusManager = LocalFocusManager.current

        //при изменении индекса анимируем переход на другую вкладку
        LaunchedEffect(selectedTabIndex) {
            pagerState.animateScrollToPage(selectedTabIndex)
            //очищаем фокус текстовых полей
            viewmodel.clearFocus(focusManager)
        }
        //при пролистывании пейджера
        LaunchedEffect(pagerState.currentPage) {
            //меняем вкладку
            selectedTabIndex = pagerState.targetPage
        }

        //сами вкладки
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(shape = RoundedCornerShape(40.dp))
                .background(color = Color.White)
        ) {
            tabItems.forEachIndexed { index, item ->
                //заголовки
                Tab(
                    modifier = Modifier
                        .height(32.dp)
                        .background(color = if (index == selectedTabIndex) MaterialTheme.colorScheme.primaryContainer else Color.White),
                    selected = index == selectedTabIndex,
                    onClick = {
                        selectedTabIndex = index
                    }
                ) {
                    Text(item)
                }
            }
        }

        //пейджер для пролистывания вкладок
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { index ->
            val scope = rememberCoroutineScope()
            val context = LocalContext.current
            //отображаем разный контент в зависимости от вкладки
            when(index) {
                0 -> {
                    //регистрация
                    SignUpScreen(
                        focusRequester = viewmodel.focusRequester,
                        onClick = { username, email, password ->
                            scope.launch {
                                val signedUp = viewmodel.signUp(username, email, password)
                                if(!signedUp) {
                                    Toast.makeText(context,
                                        context.getString(R.string.user_exists_error), Toast.LENGTH_SHORT).show()
                                }
                                else {
                                    navigateToSearch()
                                }
                            }
                        }
                    )
                }
                1 -> {
                    //вход в уже существующий аккаунт
                    SignInScreen(
                        focusRequester = viewmodel.focusRequester,
                        onClick = { username, password ->
                            scope.launch {
                                //пытаемся зайти в аккаунт
                                val signedIn = viewmodel.signIn(username, password)
                                //если неуспешно, выводим тост
                                if (!signedIn) {
                                    Toast.makeText(context,
                                        context.getString(R.string.incorrect_data_error), Toast.LENGTH_SHORT).show()
                                }
                                //иначе переходим на главную страницу
                                else {
                                    navigateToSearch()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}