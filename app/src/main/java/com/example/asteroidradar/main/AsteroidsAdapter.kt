package com.example.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.asteroidradar.databinding.AsteroidItemBinding
import com.example.asteroidradar.repository.database.Asteroid

class AsteroidsAdapter(private val clickListener: AsteroidClickListener) :
    ListAdapter<Asteroid, AsteroidsAdapter.AsteroidsViewHolder>(AsteroidsDiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidsViewHolder {
        return AsteroidsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AsteroidsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }


    class AsteroidsViewHolder private constructor(private val binding: AsteroidItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(item: Asteroid, clickListener: AsteroidClickListener){
                binding.apply {
                    asteroid = item
                    click = clickListener
                    executePendingBindings()
                }
            }
        companion object{
            fun from(parent: ViewGroup):AsteroidsViewHolder {
                val binding: AsteroidItemBinding = AsteroidItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return AsteroidsViewHolder(binding)
            }
        }
    }

    class AsteroidsDiffCallBack : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
                return newItem == oldItem
        }
    }
    class AsteroidClickListener(private val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}


