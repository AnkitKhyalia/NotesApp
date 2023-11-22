package com.example.notes_app.firebaseRealTimeDb.ui

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxScopeInstance.align
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.SemanticsProperties.ContentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notes_app.firebaseRealTimeDb.RealtimeModelResponse
import com.example.notes_app.utils.ResultState
import com.example.notes_app.utils.showMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealtimeScreen(
    isInsert:MutableState<Boolean>,
    viewModel: RealtimeViewModel= hiltViewModel()
) {

    val title = remember{ mutableStateOf("") }
    val des = remember{ mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val isDialog= remember{ mutableStateOf(false) }
    val isUpdate= remember{ mutableStateOf(false) }
    if(isInsert.value){
        AlertDialog(onDismissRequest = { isInsert.value=false },
            text = {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    TextField(value = title.value, onValueChange ={title.value=it} ,
                        placeholder = { Text(text = "Title")}
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(value = des.value, onValueChange ={des.value=it} ,
                        placeholder = { Text(text = "Description")}
                    )


                }
            },
            confirmButton = { 
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                    Button(onClick = {
                        scope.launch (Dispatchers.Main){
                            viewModel.insert(
                                RealtimeModelResponse.RealtimeItems(
                                    title.value,
                                    des.value
                                )
                            ).collect{
                                when(it){
                                    is ResultState.Success ->{
                                        context.showMsg(it.data)
                                        isDialog.value=false
                                        isInsert.value=false
                                    }
                                    is ResultState.Loading->{
                                        isDialog.value=true;
                                    }
                                    is ResultState.Failure ->{
                                        context.showMsg(it.msg.toString())
                                        isDialog.value=false
                                    }
                                }
                            }
                        }
                    }) {
                        Text(text = "Save")
                    }
                }
            }




        )
    }
    val res= viewModel.res.value
    if(isUpdate.value){
        Update(isUpdate = isUpdate, itemState =viewModel.updateRes.value, viewModel =viewModel )
    }
    if(res.item.isNotEmpty()){
        LazyColumn(){
            items(
                res.item,
                key = {
                    it.key!!
                }
            ){res->
                EachRow(itemstate = res.item!!,
                onUpdate = {
                    isUpdate.value=true
                    viewModel.setData(res)

                },
                    onDelete = {
                        scope.launch (Dispatchers.Main) {
                            viewModel.delete(res.key!!).collect{
                                when(it){
                                    is ResultState.Success ->{
                                        context.showMsg(it.data)
                                        isDialog.value=false
                                    }
                                    is ResultState.Loading->{
                                        isDialog.value=true;
                                    }
                                    is ResultState.Failure ->{
                                        context.showMsg(it.msg.toString())
                                        isDialog.value=false
                                    }
                                }
                            }
                        }
                    }

                )

            }
        }
    }
    if(res.isLoading){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center){
            CircularProgressIndicator()
        }
    }
    if(res.error.isNotEmpty()){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center){
            Text(text = res.error)
        }
    }
}

@Composable
fun EachRow(
    itemstate:RealtimeModelResponse.RealtimeItems,
    onUpdate :()->Unit={},
    onDelete:()->Unit={}
){
    Card(modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable { onUpdate() }
        ){
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)){


            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =Arrangement.SpaceBetween
            ){
                Text(text = itemstate.title!!,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
                IconButton(onClick = { onDelete()},
                    modifier = Modifier.align(CenterVertically)) {
                    androidx.compose.material3.Icon(imageVector = Icons.Default.Delete, contentDescription ="", tint = Color.Red )

                }
            }
            Text(text = itemstate.description!!,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            )
            }

        }

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Update(
    isUpdate:MutableState<Boolean>,
    itemState: RealtimeModelResponse,
    viewModel: RealtimeViewModel
) {
    val title = remember { mutableStateOf(itemState.item?.title) }
    val des = remember { mutableStateOf(itemState.item?.description) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    if(isUpdate.value){

        AlertDialog(onDismissRequest = { isUpdate.value = false },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(value = title.value!!, onValueChange = {
                        title.value = it
                    },
                        placeholder = { Text(text = "Title") }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(value = des.value!!, onValueChange = {
                        des.value = it
                    },
                        placeholder = { Text(text = "description") }
                    )
                }
            },
            confirmButton = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Center) {
                    Button(onClick = {
                        scope.launch(Dispatchers.Main) {
                            viewModel.update(
                                RealtimeModelResponse(
                                    item = RealtimeModelResponse.RealtimeItems(
                                        title.value,
                                        des.value
                                    ),
                                    key = itemState.key
                                )
                            ).collect {
                                when (it) {
                                    is ResultState.Success -> {
                                        context.showMsg(
                                            msg = it.data
                                        )
                                        isUpdate.value = false

                                    }
                                    is ResultState.Failure -> {
                                        context.showMsg(
                                            msg = it.msg.toString()
                                        )
                                    }
                                    ResultState.Loading -> {
                                    }
                                }
                            }
                        }
                    }) {
                        Text(text = "Save")
                    }
                }
            }
        )

    }

}