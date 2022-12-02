package com.example.mungnyang

import com.example.mungnyang.animalData.AdoptAnimal
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.storage.FirebaseStorage

class InfoDAO {
    var databaseReference: DatabaseReference? = null
    var photoDatabaseReference: DatabaseReference? = null
    var reviewDatabaseReference: DatabaseReference? = null
    var storage: FirebaseStorage? = null

    init {
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("Info")
        photoDatabaseReference = db.getReference("Photo")
        reviewDatabaseReference = db.getReference("Review")
    }

    fun insertInfo(info: Information?): Task<Void> {
        return databaseReference!!.push().setValue(info) //해당된 객체 줘버리기 = 이것이 INSERT 인 것임.
        // ex) insert into user(userKey, userName, userAge, userPhone) values ('keyValue', 'nameValue' ~~) 인거랑 같음
    }

    fun insertPhoto(photo: PhotoInformation?): Task<Void> {
        return photoDatabaseReference!!.push().setValue(photo)
    }

    fun insertReview(review: AdoptAnimal): Task<Void> {
        return reviewDatabaseReference!!.push().setValue(review)
    }

    //RealTime Database User Table Select
    fun selectInfo(): Query? { // 이 Query는 Cursor와 똑같다.
        return databaseReference  //이 멤버 변수를 통해서 모든 레코드들이 넘어온다. // ex) select * from User
    }

    fun selectPhoto(): Query? { // 이 Query는 Cursor와 똑같다.
        return photoDatabaseReference  //이 멤버 변수를 통해서 모든 레코드들이 넘어온다. // ex) select * from User
    }

    //RealTime database User Table Update
    fun updateInfo(infoNo: String, hashMap: HashMap<String, Any>): Task<Void> { //String의 key 값은 Firebase의 key 값과 반드시 동일해야함.
        return databaseReference!!.child(infoNo).updateChildren(hashMap)
    }
}