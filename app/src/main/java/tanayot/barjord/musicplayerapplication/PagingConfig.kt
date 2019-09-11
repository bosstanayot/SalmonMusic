package tanayot.barjord.musicplayerapplication

import androidx.paging.PagedList

object PagingConfig{
    val config = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(ApiConstant.PAGE_SIZE)
        .setPageSize(ApiConstant.PAGE_SIZE)
        .setPrefetchDistance(ApiConstant.PREFETCH)
        .build()
}