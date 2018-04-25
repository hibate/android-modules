/*
 * Copyright (C) 2018 Hibate <ycaia86@126.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * errcode.h
 *
 *  Created on: 2018/3/28
 *      Author: Hibate
 */

#ifndef JNI_UTILS_ERRCODE_H_
#define JNI_UTILS_ERRCODE_H_

#define RET_ERRNO(X)            (0X80000000|(X))

#ifndef RET_SUCCESS
#define RET_SUCCESS 0
#endif

#ifndef RET_NO_ERROR
#define RET_NO_ERROR 0
#endif

#ifndef RET_ERROR
#define RET_ERROR -1
#endif

#ifndef RET_ILLEGAL_STATE
#define RET_ILLEGAL_STATE       RET_ERRNO(0)
#endif

#ifndef RET_ILLEGAL_ARGUMENTS
#define RET_ILLEGAL_ARGUMENTS   RET_ERRNO(1)
#endif

#ifndef RET_NULL_POINT
#define RET_NULL_POINT          RET_ERRNO(2)
#endif

#endif //JNI_UTILS_ERRCODE_H_
