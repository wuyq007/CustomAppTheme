-keepnames class android.widget.AbsListView{
    android.widget.AbsListView$RecycleBin mRecycler;
}

-keepnames class android.widget.AbsListView$RecycleBin{
    void clear();
}

-keepnames class androidx.recyclerview.widget.RecyclerView{
    androidx.recyclerview.widget.RecyclerView$Recycler mRecycler;
}

-keepnames class androidx.recyclerview.widget.RecyclerView$Recycler{
    public void clear();
}