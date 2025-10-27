package com.example.smartfarm.ui.features.finance.data.remote

import com.example.smartfarm.ui.features.finance.data.local.entity.TransactionEntity
import com.example.smartfarm.ui.features.finance.data.local.entity.SyncStatus
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreFinanceDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    
    suspend fun syncTransaction(userId: String, farmId: String, transaction: TransactionEntity) {
        val transactionData = hashMapOf(
            "id" to transaction.id,
            "farmId" to farmId,
            "type" to transaction.type.name,
            "amount" to transaction.amount,
            "description" to transaction.description,
            "category" to transaction.category.name,
            "date" to transaction.date.toString(),
            "createdAt" to transaction.createdAt,
            "updatedAt" to System.currentTimeMillis()
        )
        
        firestore.collection("users")
            .document(userId)
            .collection("farms")
            .document(farmId)
            .collection("transactions")
            .document(transaction.id)
            .set(transactionData)
            .await()
    }

    suspend fun deleteTransaction(userId: String, farmId: String, transactionId: String) {
        firestore.collection("users")
            .document(userId)
            .collection("farms")
            .document(farmId)
            .collection("transactions")
            .document(transactionId)
            .delete()
            .await()
    }
    
    suspend fun fetchTransactions(userId: String, farmId: String): List<TransactionEntity> {
        val snapshot = firestore.collection("users")
            .document(userId)
            .collection("farms")
            .document(farmId)
            .collection("transactions")
            .get()
            .await()
            
        return snapshot.documents.map { document ->
            // Convert Firestore document to TransactionEntity
            // Implementation depends on your data structure
        }
    }
}