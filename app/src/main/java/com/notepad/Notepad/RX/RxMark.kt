package com.notepad.Notepad.RX

import android.util.Log
import com.notepad.Notepad.Data.Models.MarkBoolean
import io.reactivex.subjects.PublishSubject
import io.reactivex.Observable

class RxMark {

    companion object {
        private var mInstance: RxMark? = null
        fun getInstance(): RxMark? {
            if (mInstance == null) {
                mInstance = RxMark()
            }
            return mInstance
        }
    }
    private val publisherBoolean: PublishSubject<MarkBoolean> = PublishSubject.create()
    fun publishBoolean(event: MarkBoolean) {
        publisherBoolean.onNext(event)
    }


    fun listenBoolean(): Observable<MarkBoolean> {
        return publisherBoolean
    }

}