package com.healthsync.android.db;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import java.util.List;

@Dao
public interface ParametroVitalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ParametroVitalEntity entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ParametroVitalEntity> entities);

    @Update
    void update(ParametroVitalEntity entity);

    @Delete
    void delete(ParametroVitalEntity entity);

    @Query("SELECT * FROM parametro_vital_local ORDER BY fechaHora DESC")
    LiveData<List<ParametroVitalEntity>> getAllOrderByDate();

    @Query("SELECT * FROM parametro_vital_local WHERE syncStatus = 'PENDING_SYNC'")
    List<ParametroVitalEntity> getPendingSync();

    @Query("UPDATE parametro_vital_local SET syncStatus = 'SYNCED', remoteId = :remoteId WHERE localId = :localId")
    void markSynced(long localId, long remoteId);

    @Query("DELETE FROM parametro_vital_local WHERE remoteId = :remoteId")
    void deleteByRemoteId(long remoteId);
}