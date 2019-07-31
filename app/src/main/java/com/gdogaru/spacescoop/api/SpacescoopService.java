/*
 * Copyright (c) 2019 Gabriel Dogaru - gdogaru@gmail.com
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.gdogaru.spacescoop.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SpacescoopService {

    @GET("api/languages/")
    Call<String> getLanguages();


    @GET("kidsfullfeed/{lang}/{page}/")
    Call<String> getFeedPage(@Path("lang") String language, @Path("page") int page);

}
