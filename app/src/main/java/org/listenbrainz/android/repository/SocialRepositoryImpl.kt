package org.listenbrainz.android.repository

import org.listenbrainz.android.model.SearchResult
import org.listenbrainz.android.model.SimilarUserData
import org.listenbrainz.android.model.SocialData
import org.listenbrainz.android.model.SocialResponse
import org.listenbrainz.android.service.SocialService
import org.listenbrainz.android.util.ErrorUtil.getSocialErrorType
import org.listenbrainz.android.util.ErrorUtil.parseError
import org.listenbrainz.android.util.Resource
import org.listenbrainz.android.util.ResponseError
import org.listenbrainz.android.util.Utils.authHeader
import org.listenbrainz.android.util.Utils.logAndReturn
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SocialRepositoryImpl @Inject constructor(private val service: SocialService) : SocialRepository {

    /** @return Network Failure, User DNE, Success.*/
    override suspend fun getFollowers(username: String) : Resource<SocialData> =
        runCatching {
            val response = service.getFollowersData(username = username)
    
            return@runCatching if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                // Parsing server response into ApiError
                val error = parseError(response)
        
                Resource.failure(error = getSocialErrorType(error.error))
            }
    
        }.getOrElse { logAndReturn(it) }
    
    
    /** @return Network Failure, User DNE, Success.*/
    override suspend fun getFollowing(username: String) : Resource<SocialData> =
        runCatching {
            val response = service.getFollowingData(username = username)
    
            return@runCatching if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                // Parsing server response into ApiError
                val error = parseError(response)
        
                Resource.failure(error = getSocialErrorType(error.error))
            }
    
        }.getOrElse { logAndReturn(it) }
    
    
    /** @return Network Failure, User DNE, User already followed, Success.*/
    override suspend fun followUser(username: String, accessToken: String): Resource<SocialResponse> =
        runCatching {
            val response = service.followUser(username = username, authHeader = authHeader(accessToken))
    
            return@runCatching if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                // Parsing server response into ApiError
                val error = parseError(response)
        
                Resource.failure(error = getSocialErrorType(error.error))
            }
    
        }.getOrElse { logAndReturn(it) }
    
    
    /** Apparently server does not return 400 in case a user is not followed already.
     * @return Network Failure, User DNE, Success.
     */
    override suspend fun unfollowUser(username: String, accessToken: String): Resource<SocialResponse> =
        runCatching {
            val response = service.unfollowUser(username = username, authHeader = authHeader(accessToken))
            return@runCatching if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                // Parsing server response into ApiError
                val error = parseError(response)
    
                Resource.failure(error = getSocialErrorType(error.error))
            }
            
        }.getOrElse { logAndReturn(it) }
    
    
    /** @return Network Failure, User DNE, Success. */
    override suspend fun getSimilarUsers(username: String): Resource<SimilarUserData> =
        runCatching {
            val response = service.getSimilarUsersData(username = username)
            return@runCatching if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                // Parsing server response into ApiError
                val error = parseError(response)
        
                Resource.failure(error = getSocialErrorType(error.error))
            }
    
        }.getOrElse { logAndReturn(it) }
    
    
    /** @return Network Failure, [ResponseError.RATE_LIMIT_EXCEEDED], Success. */
    override suspend fun searchUser(username: String): Resource<SearchResult> =
        runCatching {
            val response = service.searchUser(username = username)
            return@runCatching if (response.isSuccessful) {
                Resource.success(response.body()!!)
            } else {
                // Parsing server response into ApiError
                val error = parseError(response)
        
                Resource.failure(error = getSocialErrorType(error.error))
            }
    
        }.getOrElse { logAndReturn(it) }
    
}