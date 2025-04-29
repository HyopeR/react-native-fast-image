import React, {useState} from 'react';
import {FlatList, SafeAreaView, StyleSheet, View} from 'react-native';
import FastImage from '@d11/react-native-fast-image';

const App = () => {
  const [mediasRemote] = useState<string[]>([
    'https://unsplash.it/400/400?image=1',
    'https://unsplash.it/400/400?image=2',
    'https://unsplash.it/400/400?image=1',
    'https://unsplash.it/400/400?image=2',
  ]);

  return (
    <SafeAreaView style={styles.wrapper}>
      <View style={{...styles.section}}>
        <FlatList
          numColumns={2}
          data={mediasRemote}
          keyExtractor={(item, index) => `item-${index}`}
          renderItem={({item, index}) => {
            const blurRadius = index > 1 ? 5 : 0;
            return (
              <View style={styles.imageWrapper}>
                <FastImage source={{ uri: item }} resizeMode={'contain'} style={styles.image} blurRadius={blurRadius} />
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
  section: {
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
});

export default App;
