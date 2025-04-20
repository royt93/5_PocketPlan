package com.fptflash.noteios.data.settings.subCategories

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fptflash.noteios.MainActivity
import com.fptflash.noteios.R
import com.fptflash.noteios.databinding.FSettingsHowToBinding
import com.fptflash.noteios.databinding.VRowHowtoBinding
import com.fptflash.noteios.databinding.VRowHowtoCatBinding

@Keep
class HowToCategory(
    val nameId: Int,
    val iconId: Int,
    val elements: ArrayList<HowToElement>,
)

@Keep
class HowToElement(
    val subNameId: Int,
    val explanationId: Int,
    var expanded: Boolean = false,
)

class SettingsHowTo : Fragment() {

    private var _fragmentSettingsHowToBinding: FSettingsHowToBinding? = null
    private val fragmentSettingsHowToBinding get() = _fragmentSettingsHowToBinding!!

    lateinit var myAdapter: HowToAdapter
    lateinit var myActivity: MainActivity

    val howToList = arrayListOf(
        HowToCategory(
            nameId = R.string.settingsBackupTitle,
            iconId = R.drawable.ic_action_backup,
            elements = arrayListOf(
                HowToElement(R.string.howtoSubExplanation, R.string.howtoBackupExplanation),
            )
        ),
        HowToCategory(
            nameId = R.string.menuTitleTasks,
            iconId = R.drawable.ic_action_todo,
            elements = arrayListOf(
                HowToElement(R.string.howtoSubEdit, R.string.howtoTodoEdit),
                HowToElement(R.string.howtoSubDelete, R.string.howtoTodoDelete),
                HowToElement(R.string.howtoSubRearrange, R.string.howtoTodoReorder),
            )
        ),
        HowToCategory(
            nameId = R.string.menuTitleNotes,
            iconId = R.drawable.ic_action_notes,
            elements = arrayListOf(
                HowToElement(R.string.howtoSubEdit, R.string.howtoNoteEdit),
                HowToElement(R.string.howtoSubFolders, R.string.howtoNoteFolder),
                HowToElement(R.string.howtoSubExplanation, R.string.howtoNoteExplanation),
            )
        ),
        HowToCategory(
            nameId = R.string.menuTitleBirthdays,
            iconId = R.drawable.ic_action_birthday,
            elements = arrayListOf(
                HowToElement(R.string.howtoSubEdit, R.string.howtoBirthdaysEdit),
                HowToElement(R.string.howtoSubDelete, R.string.howtoBirthdaysDelete),
                HowToElement(R.string.howtoSubExplanation, R.string.howtoBirthdaysExplanation),
            )
        ),

        HowToCategory(
            nameId = R.string.menuTitleShopping,
            iconId = R.drawable.ic_action_shopping_cart,
            elements = arrayListOf(
                HowToElement(R.string.howtoSubDelete, R.string.howtoShoppingDelete),
                HowToElement(R.string.howtoSubRearrange, R.string.howtoShoppingReorder),
                HowToElement(R.string.howtoSubCheckCategory, R.string.howtoShoppingCheckCategories),
                HowToElement(R.string.howtoSubLists, R.string.howtoShoppingLists),
                HowToElement(R.string.howtoSubExplanation, R.string.howtoShoppingExplanation),
            )
        ),
        HowToCategory(
            nameId = R.string.menuTitleSleep,
            iconId = R.drawable.ic_action_sleepreminder,
            elements = arrayListOf(
                HowToElement(R.string.howtoSubExplanation, R.string.howtoSleepExplanation)
            )
        ),
        HowToCategory(
            nameId = R.string.menuTitleHome,
            iconId = R.drawable.ic_action_home,
            elements = arrayListOf(
                HowToElement(R.string.howtoSubExplanation, R.string.howtoHomeExplanation)
            )
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        myActivity = activity as MainActivity
        _fragmentSettingsHowToBinding = FSettingsHowToBinding.inflate(inflater, container, false)
        val myRecycler = fragmentSettingsHowToBinding.recyclerViewHowTo

        myAdapter = HowToAdapter(this, myActivity)
        myRecycler.adapter = myAdapter

        myRecycler.layoutManager = LinearLayoutManager(activity)
        myRecycler.setHasFixedSize(true)

        // Inflate the layout for this fragment
        return fragmentSettingsHowToBinding.root
    }
}

/**
 * CATEGORY ADAPTER
 */
class HowToAdapter(private val myFragment: SettingsHowTo, val myActivity: MainActivity) :
    RecyclerView.Adapter<HowToAdapter.HowToViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HowToViewHolder {
        val rowHowtoCatBinding = VRowHowtoCatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HowToViewHolder(rowHowtoCatBinding)
    }

    @SuppressLint("InflateParams")
    override fun onBindViewHolder(holder: HowToViewHolder, position: Int) {
        val category = myFragment.howToList[position]

        val subRecyclerView = holder.binding.subRecyclerViewHowTo
        val subAdapter = SubHowToAdapter(myFragment, category, myActivity)
        subRecyclerView.layoutManager = LinearLayoutManager(myActivity)
        subRecyclerView.adapter = subAdapter

        holder.binding.tvCategoryHowTo.text = myActivity.getString(category.nameId)
        holder.binding.ivHowToCategory.setImageResource(category.iconId)
        holder.binding.cvCategory.setCardBackgroundColor(myActivity.colorForAttr(R.attr.colorHint))
    }

    override fun getItemCount() = myFragment.howToList.size

    class HowToViewHolder(rowHowtoCatBinding: VRowHowtoCatBinding) : RecyclerView.ViewHolder(rowHowtoCatBinding.root) {
        var binding = rowHowtoCatBinding
    }
}

/**
 * SUB ADAPTER
 */
class SubHowToAdapter(
    private val myFragment: SettingsHowTo,
    val category: HowToCategory,
    val myActivity: MainActivity,
) :
    RecyclerView.Adapter<SubHowToAdapter.SubHowToViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubHowToViewHolder {
        val rowHowtoBinding = VRowHowtoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubHowToViewHolder(rowHowtoBinding)
    }

    @SuppressLint("InflateParams")
    override fun onBindViewHolder(holder: SubHowToViewHolder, position: Int) {
        //Get item and its contents
        val item = category.elements[position]
        val itemName = myActivity.getString(item.subNameId)
        val itemExplanation = myActivity.getString(item.explanationId)
        val itemExpanded = item.expanded

        //set the contents to the text fields
        holder.binding.tvHowToSubTitle.text = itemName
        holder.binding.tvHowToExplanation.text = itemExplanation

        //show howto element content, depending on expansion state
        holder.binding.tvHowToExplanation.visibility = when (itemExpanded) {
            true -> View.VISIBLE
            else -> View.GONE
        }

        //Flip expansion arrow to show expansion state of category
        holder.binding.ivExpand.rotation = when (itemExpanded) {
            true -> 180f
            else -> 0f
        }

        //Onclick listener to expand howto element
        holder.binding.root.setOnClickListener {
            category.elements[position].expanded++
            myFragment.myAdapter.notifyItemChanged(myFragment.howToList.indexOf(category))
        }
    }

    override fun getItemCount() = category.elements.size

    class SubHowToViewHolder(itemViewBinding: VRowHowtoBinding) : RecyclerView.ViewHolder(itemViewBinding.root) {
        var binding = itemViewBinding
    }
}

//Override for boolean inc function, to flip it using ++
operator fun Boolean.inc() = !this
