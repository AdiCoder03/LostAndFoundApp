package com.example.lostandfoundapplication

import android.os.Bundle
import android.util.Log
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lostandfoundapplication.databinding.ActivityMyPostsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage

class MyPostsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var myPostsList : ArrayList<MyPostObject>
    private lateinit var myPostAdapter: MyPostAdapter
    private lateinit var stRef : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_posts)

        recyclerView = findViewById(R.id.myPostsRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        myPostsList = arrayListOf()

        myPostAdapter  = MyPostAdapter(myPostsList)

        recyclerView.adapter = myPostAdapter

        EventChangeListener()
    }

    private fun EventChangeListener()
    {
        stRef = FirebaseFirestore.getInstance()

        stRef.collection("Found Object Posts").whereEqualTo("userID", FirebaseAuth.getInstance().currentUser!!.uid).addSnapshotListener(object : EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if(error != null)
                {
                    Log.e("Firestore error", error.toString())
                    return
                }

                for(dc : DocumentChange in value?.documentChanges!!)
                {
                    if(dc.type == DocumentChange.Type.ADDED)
                    {
                        myPostsList.add(dc.document.toObject(MyPostObject::class.java))
                    }
                }
            }
        })

        stRef.collection("Lost Object Posts").whereEqualTo("userID", FirebaseAuth.getInstance().currentUser!!.uid).addSnapshotListener(object : EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if(error != null)
                {
                    Log.e("Firestore error", error.toString())
                    return
                }

                for(dc : DocumentChange in value?.documentChanges!!)
                {
                    if(dc.type == DocumentChange.Type.ADDED)
                    {
                        myPostsList.add(dc.document.toObject(MyPostObject::class.java))
                    }
                }
            }
        })

        myPostsList.sortWith(compareBy { it.date_time })

        myPostAdapter.notifyDataSetChanged()
    }
}
