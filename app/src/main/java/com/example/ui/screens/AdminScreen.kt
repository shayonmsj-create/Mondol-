package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.ProductEntity
import com.example.ui.theme.MondalGreen
import com.example.ui.theme.MondalLightGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    name: String,
    price: String,
    unit: String,
    category: String,
    imageUrl: String,
    description: String,
    allProducts: List<ProductEntity>,
    onNameChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onUnitChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onImageUrlChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAddProduct: () -> Unit,
    onDeleteProduct: (Long) -> Unit,
    onRestoreSampleProducts: () -> Unit
) {
    var productToDelete by remember { mutableStateOf<ProductEntity?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AddBusiness,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "মন্ডল এন্টারপ্রাইজ - অ্যাডমিন",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MondalGreen
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Card container for adding new product matching Flutter
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "নতুন প্রোডাক্ট যোগ করুন",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MondalGreen
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Product Name field
                    OutlinedTextField(
                        value = name,
                        onValueChange = onNameChange,
                        label = { Text("প্রোডাক্ট নাম") },
                        placeholder = { Text("উদা: সরিষার খাঁটি তেল") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_product_name_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Price field
                    OutlinedTextField(
                        value = price,
                        onValueChange = onPriceChange,
                        label = { Text("দাম (৳)") },
                        placeholder = { Text("উদা: ২৫০") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_product_price_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Unit / Measurement field
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = unit,
                            onValueChange = onUnitChange,
                            label = { Text("পরিমাপ/একক") },
                            placeholder = { Text("১ কেজি / ১ লিটার") },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("admin_product_unit_input"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        OutlinedTextField(
                            value = category,
                            onValueChange = onCategoryChange,
                            label = { Text("ক্যাটাগরি") },
                            placeholder = { Text("মুদি ও খাদ্য") },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("admin_product_category_input"),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Image URL field matching Flutter
                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = onImageUrlChange,
                        label = { Text("ছবির লিংক") },
                        placeholder = { Text("https://...") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Image, contentDescription = null)
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_product_image_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Quick image presets
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SuggestionChip(
                            onClick = {
                                onImageUrlChange("https://images.unsplash.com/photo-1586201375761-83865001e31c?w=500&q=80")
                                onCategoryChange("মুদি ও খাদ্য")
                                onUnitChange("১ কেজি")
                            },
                            label = { Text("চাল", fontSize = 11.sp) }
                        )
                        SuggestionChip(
                            onClick = {
                                onImageUrlChange("https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=500&q=80")
                                onCategoryChange("তেল ও ঘি")
                                onUnitChange("১ লিটার")
                            },
                            label = { Text("তেল", fontSize = 11.sp) }
                        )
                        SuggestionChip(
                            onClick = {
                                onImageUrlChange("https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=500&q=80")
                                onCategoryChange("পুষ্টিকর খাদ্য")
                                onUnitChange("৫০০ গ্রাম")
                            },
                            label = { Text("মধু", fontSize = 11.sp) }
                        )
                        SuggestionChip(
                            onClick = {
                                onImageUrlChange("https://images.unsplash.com/photo-1574323347407-f5e1ad6d020b?w=500&q=80")
                                onCategoryChange("কৃষি ও বীজ")
                                onUnitChange("১ কেজি")
                            },
                            label = { Text("বীজ", fontSize = 11.sp) }
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Add Button matching Flutter
                    Button(
                        onClick = onAddProduct,
                        colors = ButtonDefaults.buttonColors(containerColor = MondalGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("admin_add_product_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "যোগ করুন",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Inventory Management Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Inventory,
                        contentDescription = null,
                        tint = MondalGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "বর্তমান পণ্য তালিকা (${allProducts.size} টি)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MondalGreen
                        )
                    )
                }

                OutlinedButton(
                    onClick = onRestoreSampleProducts,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = ButtonDefaults.TextButtonContentPadding
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MondalGreen
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("নমুনা লোড", fontSize = 12.sp, color = MondalGreen)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (allProducts.isEmpty()) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "তালিকায় কোনো প্রোডাক্ট নেই। উপরে ফর্ম থেকে প্রোডাক্ট যোগ করুন।",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    allProducts.forEach { prod ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = prod.imageUrl,
                                    contentDescription = prod.name,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = prod.name,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "${prod.category} • ${prod.unit}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "৳ ${prod.price.toInt()}",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MondalGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }

                                IconButton(
                                    onClick = { productToDelete = prod },
                                    modifier = Modifier.testTag("delete_product_${prod.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "মুছুন",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // Delete confirmation dialog
    productToDelete?.let { prod ->
        AlertDialog(
            onDismissRequest = { productToDelete = null },
            title = { Text("প্রোডাক্ট মুছে ফেলবেন?") },
            text = { Text("\"${prod.name}\" প্রোডাক্টটি তালিকা থেকে স্থায়ীভাবে ডিলিট করতে চান?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteProduct(prod.id)
                        productToDelete = null
                    }
                ) {
                    Text("হ্যাঁ, ডিলিট করুন", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { productToDelete = null }) {
                    Text("বাতিল")
                }
            }
        )
    }
}
