package com.fptflash.noteios.data.settings.subCategories.shoppinglist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fptflash.noteios.MainActivity
import com.fptflash.noteios.R
import com.fptflash.noteios.data.fragmenttags.FT
import com.fptflash.noteios.data.settings.SettingId
import com.fptflash.noteios.data.settings.SettingsManager
import com.fptflash.noteios.data.shoppinglist.ItemTemplate
import com.fptflash.noteios.data.shoppinglist.MultiShoppingFr
import com.fptflash.noteios.databinding.FCustomItemsBinding
import com.fptflash.noteios.databinding.VRowCustomItemBinding

class CustomItemFr : Fragment() {
    private var _fragmentBinding: FCustomItemsBinding? = null
    private val fragmentBinding get() = _fragmentBinding!!

    private lateinit var myShoppingFr: MultiShoppingFr

    private lateinit var myActivity: MainActivity
    private lateinit var myMenu: Menu

    companion object {
        lateinit var myFragment: CustomItemFr
        lateinit var myAdapter: CustomItemAdapter
        lateinit var myRecycler: RecyclerView
        var deletedItem: ItemTemplate? = null

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _fragmentBinding = FCustomItemsBinding.inflate(inflater, container, false)

        myActivity = activity as MainActivity
        myShoppingFr = myActivity.getFragment(FT.SHOPPING) as MultiShoppingFr

        myRecycler = fragmentBinding.recyclerViewCustomItems
        myFragment = this
        deletedItem = null

        /**
         * Connecting Adapter, Layout-Manager and Swipe Detection to UI elements
         */

        myAdapter = CustomItemAdapter(myActivity)
        myRecycler.adapter = myAdapter
        myRecycler.layoutManager = LinearLayoutManager(activity)
        myRecycler.setHasFixedSize(true)

        val swipeHelperLeft = ItemTouchHelper(
            SwipeToDeleteCustomItem(
                direction = ItemTouchHelper.LEFT, myActivity = myActivity
            )
        )
        swipeHelperLeft.attachToRecyclerView(myRecycler)
        val swipeHelperRight = ItemTouchHelper(
            SwipeToDeleteCustomItem(
                direction = ItemTouchHelper.RIGHT, myActivity = myActivity
            )
        )
        swipeHelperRight.attachToRecyclerView(myRecycler)

        return fragmentBinding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemCustomClear -> {
                val action: () -> Unit = {
                    //remove user items from itemNameList and update act adapter so they
                    //don't show up in the add item dialog anymore
                    myActivity.userItemTemplateList.clear()
                    myActivity.userItemTemplateList.save()
                    myActivity.multiShoppingFr.refreshItemNamesAndAutoCompleteAdapter()
                    myAdapter.notifyDataSetChanged()
                    updateClearCustomListIcon()
                }
                val titleId = R.string.settingsCustomClearDialog
                myActivity.dialogConfirm(titleId, action)
            }

            R.id.itemCustomUndo -> {
                //Return if deletedItem = null, this should never happen
                if (deletedItem == null) return true
                //Re-Add item to userItemTemplateList
                deletedItem?.let {
                    myActivity.userItemTemplateList.add(it)
                }
                myActivity.userItemTemplateList.save()
                //Re-Add itemName to itemNameList
                myActivity.multiShoppingFr.refreshItemNamesAndAutoCompleteAdapter()

                deletedItem = null
                myAdapter.notifyDataSetChanged()
                updateUndoCustomIcon()
                updateClearCustomListIcon()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    fun updateClearCustomListIcon() {
        myMenu.findItem(R.id.itemCustomClear).isVisible = myActivity.userItemTemplateList.size > 0
    }

    fun updateUndoCustomIcon() {
        myMenu.findItem(R.id.itemCustomUndo).isVisible = deletedItem != null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        myMenu = menu
        inflater.inflate(R.menu.menu_custom_items, menu)
        super.onCreateOptionsMenu(menu, inflater)
        updateClearCustomListIcon()
        updateUndoCustomIcon()
    }
    //Deletes all checked tasks and animates the deletion

}

class SwipeToDeleteCustomItem(direction: Int, val myActivity: MainActivity) :
    ItemTouchHelper.SimpleCallback(0, direction) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val parsedViewHolder = viewHolder as CustomItemAdapter.CustomItemViewHolder
        val item = parsedViewHolder.myItem

        //save deleted item for undo purposes
        CustomItemFr.deletedItem = item

        //delete item from userItemTemplateList
        myActivity.userItemTemplateList.remove(item)
        myActivity.userItemTemplateList.save()

        myActivity.multiShoppingFr.refreshItemNamesAndAutoCompleteAdapter()

        //animate remove in recycler view adapter
        CustomItemFr.myAdapter.notifyItemRemoved(viewHolder.bindingAdapterPosition)

        //update options menu
        CustomItemFr.myFragment.updateUndoCustomIcon()
        CustomItemFr.myFragment.updateClearCustomListIcon()
    }
}

class CustomItemAdapter(val myActivity: MainActivity) : RecyclerView.Adapter<CustomItemAdapter.CustomItemViewHolder>() {

    private val round = SettingsManager.getSetting(SettingId.SHAPES_ROUND) as Boolean
    private val cr = myActivity.resources.getDimension(R.dimen.cornerRadius)

    override fun getItemCount() = myActivity.userItemTemplateList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomItemViewHolder {
        val rowCustomItemBinding = VRowCustomItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomItemViewHolder(rowCustomItemBinding)
    }


    @SuppressLint("SetTextI18n", "InflateParams")
    override fun onBindViewHolder(holder: CustomItemViewHolder, position: Int) {


        if (round) {
            holder.binding.cvCustom.radius = cr
        }

        val currentItem = myActivity.userItemTemplateList[holder.bindingAdapterPosition]
        holder.myItem = currentItem

        //show name
        holder.binding.tvName.text = currentItem.n

        //show category
        val id = myActivity.resources.getStringArray(R.array.categoryCodes).indexOf(currentItem.c)
        val catText = myActivity.resources.getStringArray(R.array.categoryNames)[id]
        holder.binding.tvCategory.text =
            myActivity.getString(R.string.settingsCustomCategory) + ":  " + catText

        //show unit
        holder.binding.tvUnit.text =
            myActivity.getString(R.string.settingsCustomUnit) + ": " + currentItem.s

    }

    class CustomItemViewHolder(rowCustomItemBinding: VRowCustomItemBinding) :
        RecyclerView.ViewHolder(rowCustomItemBinding.root) {
        lateinit var myItem: ItemTemplate
        var binding = rowCustomItemBinding
    }
}
