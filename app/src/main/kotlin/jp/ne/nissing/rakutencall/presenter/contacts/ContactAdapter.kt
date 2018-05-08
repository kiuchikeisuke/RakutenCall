package jp.ne.nissing.rakutencall.presenter.contacts

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import jp.ne.nissing.rakutencall.R
import jp.ne.nissing.rakutencall.data.entity.contact.Contact
import jp.ne.nissing.rakutencall.domain.contacts.ContactDto

class ContactAdapter(var entities: List<ContactDto>, val onClickEvent: (Int, Contact, Boolean) -> Unit) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_contact, parent, false))
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.onBindViewHolder(entities[position], position)
    }

    override fun getItemCount(): Int = entities.size


    inner class ContactViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        private val checkBoxImageView: ImageView by lazy<ImageView> { itemView!!.findViewById(R.id.checkBoxImageView) }
        private val displayNameTextView: TextView by lazy<TextView> { itemView!!.findViewById(R.id.displayNameTextView) }
        private val telNumberTextView: TextView by lazy<TextView> { itemView!!.findViewById(R.id.telNumberTextView) }

        fun onBindViewHolder(entity: ContactDto, position: Int) {
            checkBoxImageView.isSelected = entity.isSelected
            displayNameTextView.text = entity.contact.displayName
            telNumberTextView.text = entity.contact.phoneNumberString
            itemView.setOnClickListener {
                checkBoxImageView.isSelected = !checkBoxImageView.isSelected
                onClickEvent(position, entity.contact, checkBoxImageView.isSelected)
            }
        }
    }
}
