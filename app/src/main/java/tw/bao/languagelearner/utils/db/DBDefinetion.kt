package tw.bao.languagelearner.utils.db

/**
 * Created by bao on 2017/11/9.
 */
object DBDefinetion {

    val WORDS_DB_NAME = "words"
    val WORDS_DB_DEFAULT_VERSION = 1

    val ASSET_FILE_EXTESION = ".txt"


    //Table name
    public object TableName {
        val WORD_TABLE_BASIC = "word_basic"
        val WORD_TABLE_LIFE = "word_life"
        val WORD_TABLE_BUSINESS = "word_business"
        val WORD_TABLE_SCHOOL = "word_school"
        val WORD_TABLE_DIRTY = "word_dirty"
    }

    public val TABLE_LIST = arrayOf(TableName.WORD_TABLE_BASIC
            , TableName.WORD_TABLE_LIFE
            , TableName.WORD_TABLE_BUSINESS
            , TableName.WORD_TABLE_SCHOOL
            , TableName.WORD_TABLE_DIRTY)
}