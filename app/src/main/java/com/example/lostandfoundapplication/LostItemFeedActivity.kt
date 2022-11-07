package com.example.lostandfoundapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.protobuf.Value

class LostItemFeedActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var la: LostAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var listoflost: ArrayList<LostPost>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lost_item_feed)

        recyclerView = findViewById(R.id.lostfeedrecur)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        listoflost = arrayListOf()

        la = LostAdapter(listoflost)
        recyclerView.adapter = la

        LostFun()

    }

    private fun LostFun()
    {
        db  = FirebaseFirestore.getInstance()
        db.collection("Lost Object Posts").
        addSnapshotListener(object : EventListener<QuerySnapshot>{
            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ) {
                if(error!=null)
                {
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                for(dc: DocumentChange in value?.documentChanges!!){
                    if(dc.type == DocumentChange.Type.ADDED){
                        listoflost.add(dc.document.toObject(LostPost::class.java))


                    }
                }
                la.notifyDataSetChanged()
            }

        })
    }
}