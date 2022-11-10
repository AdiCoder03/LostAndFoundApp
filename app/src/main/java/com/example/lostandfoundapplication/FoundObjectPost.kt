package com.example.lostandfoundapplication

import com.google.type.DateTime

data class FoundObjectPost(val name : String ?= null, val phone : String ?= null, val location : String ?= null, val msg : String ?= null, val userID : String ?= null, val img_count : Int ?= 0, val date_time  : String ?= null, var doc_id : String ?= null)