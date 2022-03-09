package com.glushko.films.presentation.ui.films.anim

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.presentation.ui.films.AdapterFilms
import com.google.android.material.animation.AnimationUtils.DECELERATE_INTERPOLATOR


class FilmsItemAnimate : DefaultItemAnimator() {

    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun recordPreLayoutInformation(
        state: RecyclerView.State,
        viewHolder: RecyclerView.ViewHolder,
        changeFlags: Int,
        payloads: MutableList<Any>
    ): ItemHolderInfo {
        if (changeFlags == FLAG_CHANGED) {
            for (obj in payloads) {
                if (obj is String) {
                    return CharacterItemHolderInfo(obj)
                }
            }
        }
        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads)
    }

    inner class CharacterItemHolderInfo(val updateAction: String) : ItemHolderInfo()

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preInfo: ItemHolderInfo,
        postInfo: ItemHolderInfo
    ): Boolean {
        if (preInfo is CharacterItemHolderInfo) {
            val holder: AdapterFilms.FilmViewHolder =
                newHolder as AdapterFilms.FilmViewHolder
            if (AdapterFilms.ACTION_CLICK_LIKE == preInfo.updateAction) {
                animatePhotoLike(holder)
            }
        }
        return false
    }

    private fun animatePhotoLike(holder: AdapterFilms.FilmViewHolder) {
        holder.imgAnimate.visibility = View.VISIBLE
        holder.imgAnimate.scaleY = 0.0f
        holder.imgAnimate.scaleX = 0.0f

        val scaleLikeIcon: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            holder.imgAnimate,
            PropertyValuesHolder.ofFloat("scaleX", 0.0f, 4.0f),
            PropertyValuesHolder.ofFloat("scaleY", 0.0f, 4.0f),
            PropertyValuesHolder.ofFloat("alpha", 0.0f, 1.0f, 0.0f)
        )
        scaleLikeIcon.interpolator = DECELERATE_INTERPOLATOR
        scaleLikeIcon.duration = 2000

        val animator = AnimatorSet()
        animator.play(scaleLikeIcon)
        animator.start()
    }


}