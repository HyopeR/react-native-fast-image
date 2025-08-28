import React, {useState} from 'react';
import {FlatList, SafeAreaView, StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import FastImage from '@d11/react-native-fast-image';

const App = () => {
  const [radius, setRadius] = useState(5);
  const [medias] = useState<string[]>([
    'https://unsplash.it/400/400?image=1',
    'https://unsplash.it/400/400?image=2',
    'https://unsplash.it/400/400?image=1',
    'https://unsplash.it/400/400?image=2',
  ]);

  return (
    <SafeAreaView style={styles.wrapper}>
      <View style={styles.sectionCounter}>
        <View style={styles.counterWrapper}>
          <TouchableOpacity
            style={styles.counterButton}
            onPress={() => setRadius(p => p - 1)}
          >
            <Text style={styles.counterText}>-</Text>
          </TouchableOpacity>

          <Text style={styles.counterText}>Blur: {radius}</Text>

          <TouchableOpacity
            style={styles.counterButton}
            onPress={() => setRadius(p => p + 1)}
          >
            <Text style={styles.counterText}>+</Text>
          </TouchableOpacity>
        </View>
      </View>

      <View style={styles.sectionImage}>
        <FlatList
          numColumns={2}
          data={medias}
          keyExtractor={(item, index) => `item-${index}`}
          renderItem={({item, index}) => {
            const blurRadius = index > 1 ? radius : 0;
            return (
              <View style={styles.imageWrapper}>
                <FastImage
                  source={{ uri: item }}
                  resizeMode={'contain'}
                  blurRadius={blurRadius}
                  style={styles.image}
                />
              </View>
            );
          }}
          ItemSeparatorComponent={() => <View style={{ height: 5 }} />}
        />
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  wrapper: {
    flex: 1,
    flexDirection: 'column',
  },
  sectionImage: {
    flex: 1,
  },
  imageWrapper: {
    flex: 1 / 2,
    alignItems: 'center',
    justifyContent: 'center',
  },
  image: {
    width: 160,
    height: 160,
  },
  sectionCounter: {
    padding: 20,
  },
  counterWrapper: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  counterText: {
    fontSize: 28,
  },
  counterButton: {
    width: 50,
    height: 50,
    backgroundColor: 'gray',
    alignItems: 'center',
    justifyContent: 'center',
  },
});

export default App;
