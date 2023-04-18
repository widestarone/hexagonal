package com.hexagonal.common.util.file_url

import com.hexagonal.common.file.constant.FileUrlConstant
import com.hexagonal.common.file.constant.FileUrlPurpose

object FileUrlUtil {
    fun makeUrl(fileUrlPurpose: FileUrlPurpose, path: String, fileName: String): String {
        return fileUrlPurpose.getPath().replace(
            FileUrlConstant.S3_OBJECT_PATH_REPLACE_KEY,
            path,
        ) + fileName
    }
}
